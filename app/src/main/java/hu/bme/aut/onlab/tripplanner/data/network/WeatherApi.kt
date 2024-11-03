package hu.bme.aut.onlab.tripplanner.data.network

import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Named

interface WeatherApi {
    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String?,
        @Query("units") units: String?
    ): WeatherData?
}