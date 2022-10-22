package hu.bme.aut.onlab.tripplanner.ui.login

sealed class LoginViewState

object Loading : LoginViewState()

data class LoginContent(
    var isLoading: Boolean = true
) : LoginViewState()