package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val accountPresenter: AccountPresenter) : RainbowCakeViewModel<AccountViewState>(
    Loading
) {
    fun load() = execute {
        viewState = AccountContent(loading = true)
        val user = accountPresenter.getUser()
        if(user != null)
            viewState = AccountContent(loading = false, user = user)
    }

    fun getUserEmail() = execute {
        accountPresenter.getUserEmail()
    }

    fun signOut() = execute {
        accountPresenter.signOut()
    }

    fun changePassword() = execute {
        accountPresenter.changePassword()
    }

    fun changeEmail(context: Context, password: String?, newEmail: String?) = execute {
        accountPresenter.changeEmail(context, password, newEmail)
    }
}