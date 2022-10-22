package hu.bme.aut.onlab.tripplanner.ui.login

import android.content.Context
import android.text.Editable
import co.zsmb.rainbowcake.navigation.Navigator
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val authInteractor: AuthInteractor
) {
    suspend fun login(navigator: Navigator?, context: Context, mail: Editable, pass: Editable) = withIOContext {
        authInteractor.loginClick(navigator, context, mail, pass)
    }

    suspend fun register(context: Context, mail: Editable, pass: Editable) = withIOContext {
        authInteractor.registerClick(context, mail, pass)
    }
}