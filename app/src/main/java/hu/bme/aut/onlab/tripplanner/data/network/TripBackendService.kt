package hu.bme.aut.onlab.tripplanner.data.network

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import retrofit2.Response
import retrofit2.http.*

interface TripBackendService {
    @GET("trips")
    suspend fun getTrips(@Header("Authorization") authorization: String): Response<List<TripListItem>>

    @POST("trips")
    suspend fun addTrip(@Header("Authorization") authorization: String, @Body trip: TripListItem): Response<TripListItem>

    @PUT("trips/{id}")
    suspend fun editTrip(@Header("Authorization") authorization: String, @Path("id") id: Long, @Body trip: TripListItem): Response<TripListItem>

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Header("Authorization") authorization: String, @Path("id") id: Long): Response<Unit>

    @GET("shared")
    suspend fun getSharedByTown(
        @Header("Authorization") auth: String,
        @Query("town") town: String?    // null = összes
    ): Response<List<SharedData>>

    @POST("shared")
    suspend fun createShared(
        @Header("Authorization") auth: String,
        @Body body: SharedData
    ): Response<SharedData>

    @PUT("shared/{id}")
    suspend fun editShared(
        @Header("Authorization") auth: String,
        @Path("id") id: Long,
        @Body body: SharedData
    ): Response<SharedData>

    @DELETE("shared/{id}")
    suspend fun deleteShared(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): Response<Unit>

    @POST("shared/{id}/like")
    suspend fun likeShared(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): Response<SharedData>

    @POST("shared/{id}/unlike")
    suspend fun unlikeShared(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): Response<SharedData>
}
