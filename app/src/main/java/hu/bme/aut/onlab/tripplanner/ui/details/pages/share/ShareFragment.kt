package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog.NewShareItemDialogFragment
import hu.bme.aut.onlab.tripplanner.views.Share
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class ShareFragment : RainbowCakeFragment<ShareViewState, ShareViewModel>(), NewShareItemDialogFragment.NewShareItemDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var trip: TripListItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setShare(trip.place)
    }

    fun setTrip(trip: TripListItem) {
        this.trip = trip
    }

    override fun onUploadPost(nick: String, title: String, comment: String, image: Bitmap?) {
        viewModel.uploadPost(trip.place, nick, title, comment, image)
    }

    override fun onEditPost(item: SharedData, image: Bitmap?) {
        viewModel.editPost(trip.place, item, image)
    }

    override fun render(viewState: ShareViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is ShareContent -> Share(
                            viewState.list,
                            viewState.currentUser,
                            onAddCommentClick = ::onAddPost,
                            onEditClicked = ::onItemEdited,
                            onDeleteClicked = ::onItemRemoved,
                            onLikeClicked = ::onItemLiked
                        )
                    }.exhaustive
                }
            }
        }
    }

    private fun onAddPost() {
        if(isConnected(requireContext()))
            NewShareItemDialogFragment().show(
                childFragmentManager,
                NewShareItemDialogFragment.TAG
            )
    }

    private fun onItemEdited(item: SharedData) {
        NewShareItemDialogFragment(item).show(
            childFragmentManager,
            NewShareItemDialogFragment.TAG
        )
    }

    private fun onItemRemoved(item: SharedData) {
        viewModel.deletePost(item)
    }

    private fun onItemLiked(item: SharedData) {
        viewModel.likePost(trip.place, item)
    }
}