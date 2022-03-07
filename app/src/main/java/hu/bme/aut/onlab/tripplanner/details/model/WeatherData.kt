package hu.bme.aut.onlab.tripplanner.details.model

data class WeatherData (
    var coord: Coord,
    var weather: List<Weather>? = null,
    var main: MainWeatherData? = null,
)