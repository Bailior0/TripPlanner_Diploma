package hu.bme.aut.onlab.tripplanner.data.network.model

data class WeatherData (
    var weather: List<Weather>? = null,
    var main: MainWeatherData? = null,
)

data class Weather (
    val id: Long = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)

data class MainWeatherData (
    val temp: Float = 0f
)