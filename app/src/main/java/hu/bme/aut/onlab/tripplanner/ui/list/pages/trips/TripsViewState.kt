package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import com.google.android.gms.maps.OnMapReadyCallback
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

sealed class TripsViewState

object Loading : TripsViewState()

data class TripsContent(
    var trips: List<TripListItem> = emptyList(),
    val loading: Boolean = true
) : TripsViewState()
