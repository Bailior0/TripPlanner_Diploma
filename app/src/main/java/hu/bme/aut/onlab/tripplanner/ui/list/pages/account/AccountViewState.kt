package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

import hu.bme.aut.onlab.tripplanner.data.disk.model.User

sealed class AccountViewState

data object Loading : AccountViewState()

data class AccountContent(
    val loading: Boolean = false,
    val user: User = User()
) : AccountViewState()