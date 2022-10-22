package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem

sealed class NewTripListItemViewState

object Loading : NewTripListItemViewState()

data class NewTripListItemContent(
    var isLoading: Boolean = true
) : NewTripListItemViewState()