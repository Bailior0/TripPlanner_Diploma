package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import androidx.lifecycle.viewModelScope
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(private val tripsPresenter: TripsPresenter) : RainbowCakeViewModel<TripsViewState>(
    Loading
) {

    fun addListener() = execute {
        viewModelScope.launch {
            tripsPresenter.addListener().collect {
                viewState = TripsContent(loading = true)
                viewState = TripsContent(
                    trips = it,
                    loading = false
                )
            }
        }
    }

    fun addFB(newItem: TripListItem) = execute {
        //viewState = TripsContent(loading = true)
        tripsPresenter.addFB(newItem)
        //viewState = TripsContent(loading = false)
    }

    fun editFB(editedItem: TripListItem) = execute {
        viewState = TripsContent(loading = true)
        tripsPresenter.editFB(editedItem)
        //viewState = TripsContent(loading = false)
    }

    fun deleteFB(removedItem: TripListItem) = execute {
        //viewState = TripsContent(loading = true)
        tripsPresenter.removeFB(removedItem)
        //viewState = TripsContent(loading = false)
    }

    /*fun load() = execute {
        viewState = TripsContent(loading = true)
        viewState = TripsContent(trips = tripsPresenter.load(), loading = false)
    }

    fun edit(editedItem: TripListItem) = execute {
        viewState = TripsContent(loading = true)
        viewState = TripsContent(trips = tripsPresenter.edit(editedItem), loading = false)
    }

    fun remove(removedItem: TripListItem) = execute {
        viewState = TripsContent(trips = tripsPresenter.remove(removedItem))
    }

    fun checkerChanged(editedItem: TripListItem) = execute {
        tripsPresenter.edit(editedItem)
    }*/
}
