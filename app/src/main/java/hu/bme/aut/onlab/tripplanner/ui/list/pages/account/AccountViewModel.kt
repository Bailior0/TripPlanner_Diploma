package hu.bme.aut.onlab.tripplanner.ui.list.pages.account

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListPresenter
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val tripListPresenter: TripListPresenter) : RainbowCakeViewModel<AccountViewState>(
    Loading
) {
    fun load() = execute {
        viewState = AccountContent(loading = true)
        val userEmail = tripListPresenter.getUserEmail()
        if(userEmail != null)
            viewState = AccountContent(loading = false, userEmail = userEmail)
    }

    fun getUserEmail() = execute {
        tripListPresenter.getUserEmail()
    }

    fun signOut() = execute {
        tripListPresenter.signOut()
    }

    fun changePassword() = execute {
        tripListPresenter.changePassword()
    }

    fun changeEmail(context: Context, password: String?, newEmail: String?) = execute {
        tripListPresenter.changeEmail(context, password, newEmail)
    }
}