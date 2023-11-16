package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import com.google.android.gms.maps.model.LatLng
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

sealed class MapsViewState

data object Loading : MapsViewState()

data class TripsContent(
    val loading: Boolean = true,
    //val maps: OnMapReadyCallback? = null,
    val place: MutableList<String> = mutableListOf(),
    val coordinates: MutableList<LatLng> = mutableListOf(),
    val categories: MutableList<TripListItem.Category>  = mutableListOf()
) : MapsViewState()