package hu.bme.aut.onlab.tripplanner.ui.details.detail

import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

sealed class DetailsViewState

object Loading : DetailsViewState()

data class DetailsContent(
    var isLoading: Boolean = true,
    var trip: TripListItem? = null
) : DetailsViewState()
