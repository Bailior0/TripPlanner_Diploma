package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import android.graphics.Bitmap
import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.disk.database.TripListItemDao
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ml.Model2
import kotlinx.coroutines.flow.Flow
import org.tensorflow.lite.support.image.TensorImage
import javax.inject.Inject

class TripsInteractor @Inject constructor(private val database: TripListItemDao, private val firebaseDataSource: FirebaseDataSource) {

    suspend fun addListener(): Flow<List<TripListItem>> {
        return firebaseDataSource.getTrips()
    }

    suspend fun addFB(newItem: TripListItem) {
        firebaseDataSource.onUploadTrip(newItem)
    }

    suspend fun editFB(editedItem: TripListItem) {
        firebaseDataSource.onEditTrip(editedItem)
    }

    suspend fun removeFB(removedItem: TripListItem) {
        firebaseDataSource.onDeleteTrip(removedItem)
    }

    /*suspend fun load(): List<TripListItem> {
        /*val newsData = network.getNews()
        val newsList = addNews(newsData)
        if(newsData != null) {
            database.deleteAll()
            database.insertAll(newsList)
        }
        return newsList*/

        val data  = mutableListOf<TripListItem>()
        data.addAll(database.getAll())
        data.sortBy { it.date }
        return data
    }*/

    /*suspend fun add(newItem: TripListItem): List<TripListItem> {
        database.insert(newItem)
        firebaseDataSource.onUploadTrip(newItem)
        return load()
    }

    suspend fun edit(editedItem: TripListItem): List<TripListItem> {
        database.update(editedItem)
        firebaseDataSource.onEditTrip(editedItem)
        return load()
    }

    suspend fun remove(removedItem: TripListItem): List<TripListItem> {
        database.delete(removedItem)
        firebaseDataSource.onDeleteTrip(removedItem)
        return load()
    }*/

    /*suspend fun setCallback(context: Context): OnMapReadyCallback {
        var items: List<TripListItem> = mutableListOf()
        val connected = ConnectivityChecker.isConnected(context)
        val geocoder = Geocoder(context, Locale.getDefault())
        var matches: MutableList<Address>? = null
        val coordinates: MutableList<LatLng> = mutableListOf()
        val categories: MutableList<TripListItem.Category> = mutableListOf()
        val place: MutableList<String> = mutableListOf()
        runBlocking {
            firebaseDataSource.getTrips().collect {
                items = it
                Log.i("alma", items[0].place)

                if(connected) {
                    for (item in items) {
                        var coordinate: LatLng?

                        if (item.coordinateX == "" || item.coordinateY == "") {
                            try {
                                matches = geocoder.getFromLocationName(item.place + " " + item.country, 1)
                            } catch (e: IOException) {
                                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            coordinate = LatLng(item.coordinateX.toDouble(), item.coordinateY.toDouble())
                            coordinates.add(coordinate)
                            categories.add(item.category)
                            place.add(item.place)
                        }

                        if (!matches.isNullOrEmpty() && matches!!.size > 0) {
                            coordinate = LatLng(matches!![0].latitude, matches!![0].longitude)
                            item.coordinateX = matches!![0].latitude.toString()
                            item.coordinateX = matches!![0].longitude.toString()
                            coordinates.add(coordinate)
                            categories.add(item.category)
                            place.add(item.place)
                            firebaseDataSource.onEditTrip(item)
                        }
                    }
                }
            }


            return@runBlocking OnMapReadyCallback { googleMap ->
                googleMap.clear()
                val bld = LatLngBounds.Builder()
                if(connected) {
                    for ((index, coordinate) in coordinates.withIndex()) {
                        when(categories[index]) {
                            TripListItem.Category.OUTDOORS -> googleMap.addMarker(
                                MarkerOptions().position(coordinate).title(place[index]).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                            TripListItem.Category.BEACHES -> googleMap.addMarker(
                                MarkerOptions().position(coordinate).title(place[index]).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))

                            TripListItem.Category.SIGHTSEEING -> googleMap.addMarker(
                                MarkerOptions().position(coordinate).title(place[index]).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))

                            TripListItem.Category.SKIING -> googleMap.addMarker(
                                MarkerOptions().position(coordinate).title(place[index]).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                            TripListItem.Category.BUSINESS -> googleMap.addMarker(
                                MarkerOptions().position(coordinate).title(place[index]).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                        }
                        bld.include(coordinate)
                    }
                }
                if (items.isNotEmpty() && connected && !matches.isNullOrEmpty()) {
                    val bounds = bld.build()
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1000,1000, 70))
                }
                googleMap.setMapStyle(MapStyleOptions(context.getString(R.string.style_json)))
            }

        }


        return TODO("Provide the return value")
    }*/

    fun identify(image: Bitmap, context: Context): String {
        val model = Model2.newInstance(context)

        val tensorImage = TensorImage.fromBitmap(image)

        val outputs = model.process(tensorImage)
        val probability = outputs.probabilityAsCategoryList

        model.close()
        val maxScore = probability.maxByOrNull { p -> p.score }!!

        return "Prediction:" + "\n" + maxScore.label + "\n" + "Probability: " + (maxScore.score * 10).toInt() + "%"
    }
}