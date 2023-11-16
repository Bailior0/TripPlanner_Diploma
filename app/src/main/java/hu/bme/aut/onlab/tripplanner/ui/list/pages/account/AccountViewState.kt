package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

sealed class AccountViewState

data object Loading : AccountViewState()

data class AccountContent(
    val loading: Boolean = false,
    val userEmail: String = ""
) : AccountViewState()