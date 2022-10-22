package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem

import co.zsmb.rainbowcake.base.RainbowCakeViewModel

class NewTripListItemViewModel : RainbowCakeViewModel<NewTripListItemViewState>(
    Loading
) {

    fun setNewTripListItem() {
        viewState = NewTripListItemContent(false)
    }
}