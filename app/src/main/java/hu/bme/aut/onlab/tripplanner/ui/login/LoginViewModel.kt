package hu.bme.aut.onlab.tripplanner.ui.login

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import co.zsmb.rainbowcake.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginPresenter: LoginPresenter) : RainbowCakeViewModel<LoginViewState>(Loading) {

    fun setLogin() {
        viewState = LoginContent(false)
    }

    fun login(navigator: Navigator?, context: Context, mail: String, pass: String) = execute {
        loginPresenter.login(navigator, context, mail, pass)
    }

    fun register(context: Context, mail: String, pass: String) = execute {
        loginPresenter.register(context, mail, pass)
    }
}