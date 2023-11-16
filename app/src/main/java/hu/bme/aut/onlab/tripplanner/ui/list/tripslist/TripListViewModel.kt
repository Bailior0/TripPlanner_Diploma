package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(private val tripListPresenter: TripListPresenter) : RainbowCakeViewModel<TripListViewState>(
    Loading
) {
    fun setNav() {
        viewState = TripsContent(isLoading = false)
    }

    /*fun add(newItem: TripListItem) = execute {
        viewState = TripsContent(trips = tripListPresenter.add(newItem), false)
    }*/

    fun getUserEmail(): String? = runBlocking {
        return@runBlocking tripListPresenter.getUserEmail()
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
