package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem.NewTripListItemDialogFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.maps.MapsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.recommendation.RecommendationFragment
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.nav.MainScreenView

@AndroidEntryPoint
class TripListFragment: RainbowCakeFragment<TripListViewState, TripListViewModel>()/*, NewTripListItemDialogFragment.NewTripListItemDialogListener*/ {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapsFragment
    private lateinit var identifierFragment: IdentifierFragment
    private lateinit var recommendationFragment: RecommendationFragment
    private lateinit var accountFragment: AccountFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapsFragment()
        identifierFragment = IdentifierFragment()
        recommendationFragment = RecommendationFragment()
        accountFragment = AccountFragment()

        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setNav()
    }

    override fun render(viewState: TripListViewState) {
        (view as ComposeView).setContent {
            when (viewState) {
                is Loading -> FullScreenLoading()
                is TripsContent -> {
                    MainScreenView(
                        parentFragmentManager,
                        tripsFragment,
                        calendarFragment,
                        mapFragment,
                        identifierFragment,
                        recommendationFragment,
                        accountFragment,
                    )
                }
            }.exhaustive
        }
    }

    private fun onNewItemDialog() {
        NewTripListItemDialogFragment().show(
            childFragmentManager,
            NewTripListItemDialogFragment.TAG
        )
    }
}