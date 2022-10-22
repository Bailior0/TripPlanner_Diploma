package hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog

import co.zsmb.rainbowcake.base.RainbowCakeViewModel

class ShareItemViewModel  : RainbowCakeViewModel<ShareItemViewState>(
    Loading
) {

    fun setShareItem() {
        viewState = ShareItemContent(false)
    }
}