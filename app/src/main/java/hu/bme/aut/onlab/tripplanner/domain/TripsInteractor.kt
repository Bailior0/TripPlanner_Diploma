package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import hu.bme.aut.onlab.tripplanner.data.datasource.BackendDataSource
import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.disk.database.TripListItemDao
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ml.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.core.graphics.scale

class TripsInteractor @Inject constructor(
    private val database: TripListItemDao,
    private val firebaseDataSource: FirebaseDataSource,
    private val backendDataSource: BackendDataSource
) {

    var useBackend = true

    suspend fun getTrips(): List<TripListItem> {
        return if (useBackend) {
            backendDataSource.getTrips()
        } else {
            firebaseDataSource.getTripsOnce()
        }
    }

    private suspend fun refreshFromBackendAndEmit(
        channel: kotlinx.coroutines.channels.ProducerScope<List<TripListItem>>
    ) {
        try {
            val fresh = backendDataSource.getTrips()
            database.deleteAll()
            database.insertAll(fresh)
            channel.trySend(fresh).isSuccess
        } catch (e: Exception) {
            Log.e("TripsSSE", "refreshFromBackend failed: ${e.message}")
            val local = database.getAll()
            channel.trySend(local).isSuccess
        }
    }

    suspend fun addListener(): Flow<List<TripListItem>> = callbackFlow {
        val channel = this
        val gson = Gson()

        suspend fun emitLocal() {
            val local = database.getAll()
            channel.trySend(local).isSuccess
        }

        emitLocal()

        if (!useBackend) {
            val job = launch(Dispatchers.IO) {
                firebaseDataSource.getTrips().collectLatest {
                    channel.trySend(it).isSuccess
                }
            }

            awaitClose {
                job.cancel()
            }
            return@callbackFlow
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val sseJob = launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val token = firebaseDataSource.getCurrentIdToken()
                    val request = Request.Builder()
                        .url("http://<own address>:8080/trips/stream") //saját backend címe
                        .header("Authorization", "Bearer $token")
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            Log.e("TripsSSE", "HTTP error: ${response.code}")
                            emitLocal()
                            delay(2000)
                            return@use
                        }

                        val source = response.body?.source() ?: run {
                            Log.e("TripsSSE", "Empty response body")
                            emitLocal()
                            delay(2000)
                            return@use
                        }

                        val listType = object : TypeToken<List<TripListItem>>() {}.type
                        var eventName: String? = null

                        while (isActive) {
                            val line = try {
                                source.readUtf8Line()
                            } catch (e: Exception) {
                                Log.e("TripsSSE", "Connection lost: ${e.message}")
                                break
                            }

                            if (line == null) {
                                Log.d("TripsSSE", "SSE stream ended by server")
                                break
                            }

                            if (line.isBlank()) {
                                continue
                            }

                            if (line.startsWith("event:")) {
                                eventName = line.removePrefix("event:").trim()
                                continue
                            }

                            if (!line.startsWith("data:")) continue
                            val data = line.removePrefix("data:").trim()

                            when (eventName) {
                                "init" -> {
                                    try {
                                        val trips: List<TripListItem> =
                                            gson.fromJson(data, listType)

                                        database.deleteAll()
                                        database.insertAll(trips)
                                        channel.trySend(trips).isSuccess
                                    } catch (e: Exception) {
                                        Log.e("TripsSSE", "Init parse error: ${e.message}")
                                        emitLocal()
                                    }
                                }

                                "trip-event" -> {
                                    try {
                                        val jsonObj: JsonObject =
                                            JsonParser.parseString(data).asJsonObject
                                        val type = jsonObj["type"].asString

                                        Log.d("TripsSSE", "Trip event received: $type")
                                        refreshFromBackendAndEmit(channel)
                                    } catch (e: Exception) {
                                        Log.e("TripsSSE", "Event parse error: ${e.message}")
                                        emitLocal()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TripsSSE", "SSE error: ${e.message}")
                    emitLocal()
                    delay(2000)
                }
            }
        }

        awaitClose {
            sseJob.cancel()
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addTrip(newItem: TripListItem) {
        if (useBackend) {
            backendDataSource.onUploadTrip(newItem)
        } else {
            firebaseDataSource.onUploadTrip(newItem)
            database.insert(newItem)
        }
    }

    suspend fun editTrip(editedItem: TripListItem) {
        if (useBackend) {
            backendDataSource.onEditTrip(editedItem)
            database.update(editedItem)
        } else {
            firebaseDataSource.onEditTrip(editedItem)
            database.update(editedItem)
        }
    }

    suspend fun removeTrip(removedItem: TripListItem) {
        if (useBackend) {
            backendDataSource.onDeleteTrip(removedItem)
        } else {
            firebaseDataSource.onDeleteTrip(removedItem)
        }
        database.delete(removedItem)
    }




    private fun loadLabels(context: Context): List<String> {
        return context.assets.open("landmark_names.txt")
            .bufferedReader()
            .readLines()
    }

    fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.max()
        val expValues = logits.map { x -> kotlin.math.exp((x - maxLogit).toDouble()) }
        val sumExp = expValues.sum()
        return expValues.map { it.toFloat() / sumExp.toFloat() }.toFloatArray()
    }

    fun identify(bitmap: Bitmap, context: Context): Category {
        val model = Model4.newInstance(context)
        val resized = bitmap.scale(256, 256)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resized)
        val inputBuffer = tensorImage.tensorBuffer
        val outputs = model.process(inputBuffer)
        val logits = outputs.outputFeature0AsTensorBuffer.floatArray
        val probs = softmax(logits)
        val maxIndex = probs.indices.maxBy { probs[it] }
        val labels = loadLabels(context)
        val label = labels[maxIndex]
        val confidence = probs[maxIndex]
        model.close()
        return Category(label, confidence)
    }
}
