package hu.bme.aut.onlab.tripplanner.data.mapdirections

import hu.bme.aut.onlab.tripplanner.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapDirectionsService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") originLatLng: String,
        @Query("destination") destinationLatLang: String,
        @Query("key") apiKey: String = BuildConfig.MAP_API_KEY
    ): Response<DirectionsDto>
}