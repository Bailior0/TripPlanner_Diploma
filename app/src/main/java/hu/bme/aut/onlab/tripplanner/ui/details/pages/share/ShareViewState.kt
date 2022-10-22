package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData

sealed class ShareViewState

object Loading : ShareViewState()

data class ShareContent(
    var list: List<SharedData> = emptyList(),
    var isLoading: Boolean = true
) : ShareViewState()
