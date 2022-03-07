package hu.bme.aut.onlab.tripplanner.details.data

import hu.bme.aut.onlab.tripplanner.details.model.WeatherData

interface WeatherDataHolder {
    fun getWeatherData(): WeatherData?
}