package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val sharePresenter: SharePresenter
) : RainbowCakeViewModel<ShareViewState>(Loading) {

    fun setShare(place: String) = execute {
        val user = sharePresenter.getCurrentUser()
        viewModelScope.launch {
            sharePresenter.getItems(place).collect {
                viewState = ShareContent(isLoading = true)
                viewState = ShareContent(
                    list = it,
                    currentUser = user,
                    isLoading = false
                )
            }
        }
    }

    fun uploadPost(place: String, nick: String, title: String, comment: String, image: Bitmap?) = execute {
        sharePresenter.uploadPost(place, nick, title, comment, image)
    }

    fun editPost(place: String, item: SharedData, image: Bitmap?) = execute {
        val user = sharePresenter.getCurrentUser()
        sharePresenter.editPost(item, image)
        viewState = ShareContent(
            list = sharePresenter.getItemsOnce(place),
            currentUser = user,
            isLoading = true
        )
    }

    fun deletePost(item: SharedData) = execute {
        sharePresenter.deletePost(item)
    }

    fun likePost(place: String, item: SharedData) = execute {
        val user = sharePresenter.getCurrentUser()
        sharePresenter.likePost(item)
        viewState = ShareContent(
            list = sharePresenter.getItemsOnce(place),
            currentUser = user,
            isLoading = true
        )
    }
}
