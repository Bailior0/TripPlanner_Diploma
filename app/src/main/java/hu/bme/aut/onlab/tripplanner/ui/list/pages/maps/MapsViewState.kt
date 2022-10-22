package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import com.google.android.gms.maps.OnMapReadyCallback

sealed class MapsViewState

object Loading : MapsViewState()

data class TripsContent(
    val loading: Boolean = true,
    val maps: OnMapReadyCallback? = null
) : MapsViewState()