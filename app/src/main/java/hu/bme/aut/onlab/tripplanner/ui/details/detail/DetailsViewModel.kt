package hu.bme.aut.onlab.tripplanner.ui.details.detail

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

class DetailsViewModel  : RainbowCakeViewModel<DetailsViewState>(
    Loading
) {
    fun setTrip(trip: TripListItem) {
        viewState = DetailsContent(false, trip)
    }
}
