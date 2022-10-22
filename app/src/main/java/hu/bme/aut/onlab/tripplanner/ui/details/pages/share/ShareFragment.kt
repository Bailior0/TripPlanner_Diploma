package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.databinding.FragmentShareBinding
import hu.bme.aut.onlab.tripplanner.ui.details.pages.sharedialog.NewShareItemDialogFragment

@AndroidEntryPoint
class ShareFragment : RainbowCakeFragment<ShareViewState, ShareViewModel>(),
    ShareAdapter.ShareListItemClickListener, NewShareItemDialogFragment.NewShareItemDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentShareBinding
    private lateinit var trip: TripListItem
    private lateinit var shareAdapter: ShareAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShareBinding.inflate(layoutInflater, container, false)
        shareAdapter = ShareAdapter(requireContext(), this)

        with(binding) {
            rvMain.adapter = shareAdapter

            rvMain.layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
                stackFromEnd = true
            }

            fab.setOnClickListener{
                if(isConnected(requireContext()))
                    NewShareItemDialogFragment().show(
                        childFragmentManager,
                        NewShareItemDialogFragment.TAG
                    )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setShare(trip.place)
    }

    fun setTrip(trip: TripListItem) {
        this.trip = trip
    }

    override fun onItemEdited(item: SharedData) {
        NewShareItemDialogFragment(item).show(
            childFragmentManager,
            NewShareItemDialogFragment.TAG
        )
    }

    override fun onItemLiked(item: SharedData) {
        viewModel.likePost(trip.place, item)
    }

    override fun onItemRemoved(item: SharedData) {
        viewModel.deletePost(trip.place, item)
    }

    override fun onUploadPost(nick: String, title: String, comment: String) {
        viewModel.uploadPost(trip.place, nick, title, comment)
    }

    override fun onEditPost(item: SharedData) {
        viewModel.editPost(trip.place, item)
    }

    override fun render(viewState: ShareViewState) {
        when(viewState) {
            is Loading -> {}
            is ShareContent -> {
                shareAdapter.submitList(viewState.list)
            }
        }.exhaustive
    }
}