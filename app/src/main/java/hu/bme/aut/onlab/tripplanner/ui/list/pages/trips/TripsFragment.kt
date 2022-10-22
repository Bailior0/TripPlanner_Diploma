package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import co.zsmb.rainbowcake.navigation.navigator
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTripsBinding
import hu.bme.aut.onlab.tripplanner.ui.details.detail.DetailsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem.NewTripListItemDialogFragment
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListFragment

@AndroidEntryPoint
class TripsFragment: RainbowCakeFragment<TripsViewState, TripsViewModel>(),
    TripListAdapter.TripListItemClickListener, NewTripListItemDialogFragment.EditTripListItemDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentTripsBinding
    private lateinit var adapter: TripListAdapter
    private lateinit var tripListFragment: TripListFragment
    private var state: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTripsBinding.inflate(layoutInflater, container, false)

        initRecyclerView()
        viewModel.load()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = TripListAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this.context)
        binding.rvMain.adapter = adapter
    }

    override fun render(viewState: TripsViewState) {
        when(viewState) {
            is Loading -> {}
            is TripsContent -> { adapter.submitList(viewState.trips) }
        }.exhaustive
    }

    fun setAdapter(trips: List<TripListItem>) {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        if(isAdded)
            adapter.submitList(trips)
    }

    fun setParent(tripListFragment: TripListFragment) {
        this.tripListFragment = tripListFragment
    }

    override fun onItemChanged(item: TripListItem) {
        viewModel.checkerChanged(item)
    }

    override fun onItemRemoved(item: TripListItem) {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        viewModel.remove(item)
        tripListFragment.onItemChanged()
    }

    override fun onItemEdited(item: TripListItem) {
        NewTripListItemDialogFragment(item).show(
            childFragmentManager,
            NewTripListItemDialogFragment.TAG
        )
    }

    override fun onTripListItemEdited(editedItem: TripListItem) {
        if(state != null)
            tripListFragment.tripListPagerAdapter.restoreState(state!!)
        viewModel.edit(editedItem)
        tripListFragment.onItemChanged()
    }

    override fun onTripSelected(tripListItem: TripListItem) {
        state = tripListFragment.tripListPagerAdapter.saveState()
        navigator?.add(DetailsFragment.newInstance(tripListItem))
    }
}