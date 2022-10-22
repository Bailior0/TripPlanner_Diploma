package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.NetworkRepo
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import javax.inject.Inject

class DetailsInteractor @Inject constructor(private val network: NetworkRepo) {

    suspend fun getWeather(place: String, context: Context): WeatherData? {
        if(isConnected(context))
            return network.getWeather(place)
        else
            return null
    }
}