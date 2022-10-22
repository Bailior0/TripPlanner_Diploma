package hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange

import co.zsmb.rainbowcake.base.RainbowCakeViewModel

class AuthChangeViewModel : RainbowCakeViewModel<AuthChangeViewState>(
    Loading
) {

    fun setAuthChange() {
        viewState = AuthChangeContent(false)
    }
}