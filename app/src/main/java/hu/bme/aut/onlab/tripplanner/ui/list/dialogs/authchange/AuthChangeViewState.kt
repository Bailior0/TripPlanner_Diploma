package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange

sealed class AuthChangeViewState

object Loading : AuthChangeViewState()

data class AuthChangeContent(
    var isLoading: Boolean = true
) : AuthChangeViewState()