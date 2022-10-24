package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

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

    fun uploadPost(place: String, nick: String, title: String, comment: String) = execute {
        sharePresenter.uploadPost(place, nick, title, comment)
    }

    fun editPost(place: String, item: SharedData) = execute {
        val user = sharePresenter.getCurrentUser()
        sharePresenter.editPost(place, item)
        viewState = ShareContent(
            list = sharePresenter.getItemsOnce(place),
            currentUser = user,
            isLoading = true
        )
    }

    fun deletePost(place: String, item: SharedData) = execute {
        sharePresenter.deletePost(place, item)
    }

    fun likePost(place: String, item: SharedData) = execute {
        val user = sharePresenter.getCurrentUser()
        sharePresenter.likePost(place, item)
        viewState = ShareContent(
            list = sharePresenter.getItemsOnce(place),
            currentUser = user,
            isLoading = true
        )
    }
}
