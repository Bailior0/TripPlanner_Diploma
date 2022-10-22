package hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog

sealed class ShareItemViewState

object Loading : ShareItemViewState()

data class ShareItemContent(
    var isLoading: Boolean = true
) : ShareItemViewState()