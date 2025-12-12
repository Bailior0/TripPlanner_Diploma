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
        tripsPresenter.addFB(newItem)
    }

    fun editFB(editedItem: TripListItem) = execute {
        viewState = TripsContent(loading = true)
        tripsPresenter.editFB(editedItem)
    }

    fun deleteFB(removedItem: TripListItem) = execute {
        tripsPresenter.removeFB(removedItem)
    }
}
