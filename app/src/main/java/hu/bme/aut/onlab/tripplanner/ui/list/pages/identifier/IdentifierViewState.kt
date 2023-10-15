package hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier

sealed class IdentifierViewState

object Loading : IdentifierViewState()

data class IdentifierContent(
    var prediction: String = "",
    val loading: Boolean = false
) : IdentifierViewState()