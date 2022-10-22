package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

sealed class TripListViewState

object Loading : TripListViewState()

data class TripsContent(
    var trips: List<TripListItem> = emptyList(),
    var isLoading: Boolean = true
) : TripListViewState()
