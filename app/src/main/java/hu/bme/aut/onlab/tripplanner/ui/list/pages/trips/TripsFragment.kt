package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import android.os.Bundle
import android.os.Parcelable
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
import co.zsmb.rainbowcake.navigation.navigator
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ui.details.detail.DetailsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem.NewTripListItemDialogFragment
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListFragment
import hu.bme.aut.onlab.tripplanner.views.Trips
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class TripsFragment: RainbowCakeFragment<TripsViewState, TripsViewModel>(),
    NewTripListItemDialogFragment.EditTripListItemDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var tripListFragment: TripListFragment
    private var state: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load()
    }

    fun setAdapter() {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        if(isAdded)
            viewModel.load()
    }

    fun setParent(tripListFragment: TripListFragment) {
        this.tripListFragment = tripListFragment
    }

    override fun render(viewState: TripsViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        is Loading -> FullScreenLoading()
                        is TripsContent -> Trips(
                            viewState.trips,
                            onItemClicked = ::onTripSelected,
                            onItemChanged = ::onItemChanged,
                            onEditClicked = ::onItemEdited,
                            onDeleteClicked = ::onItemRemoved
                        )
                    }.exhaustive
                }
            }
        }
    }

    private fun onItemChanged(item: TripListItem) {
        viewModel.checkerChanged(item)
    }

    private fun onItemEdited(item: TripListItem) {
        NewTripListItemDialogFragment(item).show(
            childFragmentManager,
            NewTripListItemDialogFragment.TAG
        )
    }

    private fun onItemRemoved(item: TripListItem) {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        viewModel.remove(item)
        tripListFragment.onItemChanged()
    }

    private fun onTripSelected(tripListItem: TripListItem) {
        state = tripListFragment.tripListPagerAdapter.saveState()
        navigator?.add(DetailsFragment.newInstance(tripListItem),
            enterAnim = 0,
            exitAnim = 0,
            popEnterAnim = 0,
            popExitAnim = 0
        )
    }

    override fun onTripListItemEdited(editedItem: TripListItem) {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        viewModel.edit(editedItem)
        tripListFragment.onItemChanged()
    }
}