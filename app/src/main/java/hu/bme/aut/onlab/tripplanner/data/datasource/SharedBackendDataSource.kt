package hu.bme.aut.onlab.tripplanner.data.datasource

import hu.bme.aut.onlab.tripplanner.data.network.SharedBackendService
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import android.graphics.Bitmap
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SharedBackendDataSource @Inject constructor(
    private val backend: SharedBackendService,
    private val firebase: FirebaseDataSource
) {

    suspend fun getPosts(town: String): Flow<List<SharedData>> = callbackFlow {
        val channel = this
        val gson = Gson()

        val token = firebase.getCurrentIdToken()
        val url = "http://<own address>:8080/shared/stream?town=$town" //saját backend címe

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val call = client.newCall(request)

        val job = launch(Dispatchers.IO) {
            var current = mutableListOf<SharedData>()

            try {
                call.execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("SharedSSE", "HTTP error: ${response.code}")
                        return@use
                    }

                    val body = response.body ?: return@use
                    val source = body.source()

                    val listType = object : TypeToken<List<SharedData>>() {}.type
                    var eventName: String? = null

                    while (isActive) {
                        val line = try {
                            source.readUtf8Line()
                        } catch (e: Exception) {
                            Log.e("SharedSSE", "Connection lost: ${e.message}")
                            break
                        }

                        if (line == null) {
                            Log.d("SharedSSE", "SSE stream ended by server")
                            break
                        }

                        if (line.isBlank()) continue

                        if (line.startsWith("event:")) {
                            eventName = line.removePrefix("event:").trim()
                            continue
                        }

                        if (!line.startsWith("data:")) continue
                        val json = line.removePrefix("data:").trim()

                        try {
                            when (eventName) {
                                "init" -> {
                                    val items: List<SharedData> = gson.fromJson(json, listType)
                                    val filtered = items.filter { it.town == town }

                                    current = filtered.toMutableList()
                                    channel.trySend(current.toList()).isSuccess
                                }

                                "shared-add" -> {
                                    val post: SharedData = gson.fromJson(json, SharedData::class.java)
                                    if (post.town == town) {
                                        current.add(post)
                                        channel.trySend(current.toList()).isSuccess
                                    }
                                }

                                "shared-edit" -> {
                                    val post: SharedData = gson.fromJson(json, SharedData::class.java)
                                    if (post.town != town) {
                                        continue
                                    }

                                    val idx = current.indexOfFirst { it.id == post.id }
                                    if (idx >= 0) {
                                        current[idx] = post
                                    } else {
                                        current.add(post)
                                    }

                                    channel.trySend(current.toList())
                                }


                                "shared-delete" -> {
                                    val obj: JsonObject =
                                        JsonParser.parseString(json).asJsonObject
                                    val deleteId = obj["id"].asString
                                    current.removeAll { it.id == deleteId }
                                    channel.trySend(current.toList()).isSuccess
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("SharedSSE", "Parse error: ${e.message} json=$json")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedSSE", "SSE fatal error: ${e.message}")
            } finally {
                channel.close()
            }
        }

        awaitClose {
            call.cancel()
            job.cancel()
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPostsOnce(town: String): List<SharedData> {
        val token = firebase.getCurrentIdToken()
        return backend.getByTown("Bearer $token", town)
    }

    suspend fun uploadPost(
        town: String,
        nick: String,
        title: String,
        body: String,
        image: Bitmap?
    ) {
        val token = firebase.getCurrentIdToken()
        val imageId = uploadImageIfNeeded(image)

        val user = firebase.getCurrentUser()
        val uid = user.documents.first().id
        val authorName = user.documents.first().getString("name")

        val dto = SharedData(
            id = null,
            uid = uid,
            author = authorName,
            nickname = nick,
            title = title,
            body = body,
            liked = mutableListOf(),
            town = town,
            pic = imageId
        )

        backend.create("Bearer $token", dto)
    }

    suspend fun editPost(item: SharedData, image: Bitmap?, deleteImage: Boolean) {
        val token = firebase.getCurrentIdToken()

        if (deleteImage) {
            item.pic = null
        } else {
            val newImage = uploadImageIfNeeded(image)
            if (newImage.isNotEmpty()) {
                item.pic = newImage
            }
        }

        backend.edit("Bearer $token", item)
    }

    suspend fun deletePost(item: SharedData) {
        val token = firebase.getCurrentIdToken()
        backend.delete("Bearer $token", item.id!!)
    }

    suspend fun likePost(item: SharedData) {
        val token = firebase.getCurrentIdToken()
        backend.like("Bearer $token", item.id!!)
    }

    private suspend fun uploadImageIfNeeded(image: Bitmap?): String {
        if (image == null) return ""
        return withContext(Dispatchers.IO) {
            firebase.uploadImageForComments(image)
        }
    }
}
