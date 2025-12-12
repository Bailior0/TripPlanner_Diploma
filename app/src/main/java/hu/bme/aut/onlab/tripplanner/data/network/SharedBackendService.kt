package hu.bme.aut.onlab.tripplanner.data.network

import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import retrofit2.http.*

interface SharedBackendService {

    @GET("shared")
    suspend fun getByTown(
        @Header("Authorization") token: String,
        @Query("town") town: String
    ): List<SharedData>

    @POST("shared")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body dto: SharedData
    ): SharedData

    @PUT("shared")
    suspend fun edit(
        @Header("Authorization") token: String,
        @Body dto: SharedData
    ): SharedData

    @DELETE("shared/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )

    @POST("shared/{id}/like")
    suspend fun like(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SharedData
}
