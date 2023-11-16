package hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier

import android.content.Context
import android.graphics.Bitmap
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IdentifierViewModel @Inject constructor(private val identifierPresenter: IdentifierPresenter) : RainbowCakeViewModel<IdentifierViewState>(
    Loading
) {

    fun load() = execute {
        viewState = IdentifierContent(loading = true)
        viewState = IdentifierContent(prediction = "", loading = false)
    }

    fun identify(image: Bitmap, context: Context) = execute {
        viewState = IdentifierContent(loading = true)
        viewState = IdentifierContent(prediction = identifierPresenter.identify(image, context), image = image, loading = false)
    }
}