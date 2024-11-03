package hu.bme.aut.onlab.tripplanner.data.network

import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import javax.inject.Inject
import javax.inject.Named

class NetworkRepo @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeather(city: String?): WeatherData? {
        return api.getWeather(city, "metric")
    }
}