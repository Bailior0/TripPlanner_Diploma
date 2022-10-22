package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(private val tripsPresenter: TripsPresenter) : RainbowCakeViewModel<TripsViewState>(
    Loading
) {

    fun load() = execute {
        viewState = TripsContent(trips = tripsPresenter.load())
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
    }
}
