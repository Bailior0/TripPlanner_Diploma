package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsPresenter: MapsPresenter) : RainbowCakeViewModel<MapsViewState>(
    Loading
) {

    fun setMap(context: Context) = execute {
        viewState = TripsContent(loading = true)
        viewState = TripsContent(maps = mapsPresenter.setCallback(context), loading = false)
    }
}