package hu.bme.aut.onlab.tripplanner.ui.details.pages.information

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData

sealed class InformationViewState

object Loading : InformationViewState()

data class DetailsContent(
    var isLoading: Boolean = true,
    var trip: TripListItem? = null
) : InformationViewState()

data class WeatherContent(
    var isLoading: Boolean = true,
    var trip: TripListItem? = null,
    var weather: WeatherData? = null
) : InformationViewState()