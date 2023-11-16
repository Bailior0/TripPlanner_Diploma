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
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.nav.MainScreenView

@AndroidEntryPoint
class TripListFragment: RainbowCakeFragment<TripListViewState, TripListViewModel>()/*, NewTripListItemDialogFragment.NewTripListItemDialogListener*/ {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapsFragment
    private lateinit var identifierFragment: IdentifierFragment
    private lateinit var accountFragment: AccountFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapsFragment()
        identifierFragment = IdentifierFragment()
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
                    //tripsFragment.setAdapter()
                    //calendarFragment.changeCalendar()
                    //mapFragment.changeMap()
                    MainScreenView(
                        parentFragmentManager,
                        tripsFragment,
                        calendarFragment,
                        mapFragment,
                        identifierFragment,
                        accountFragment,
                        //onFabClicked = ::onNewItemDialog,
                    )
                }
            }.exhaustive
        }
    }

    /*override fun onTripListItemCreated(newItem: TripListItem) {
        viewModel.add(newItem)
    }*/

    private fun onNewItemDialog() {
        NewTripListItemDialogFragment().show(
            childFragmentManager,
            NewTripListItemDialogFragment.TAG
        )
    }

    /*fun onItemChanged() {
        calendarFragment.changeCalendar()
        mapFragment.changeMap()
    }*/
}

/*@AndroidEntryPoint
class TripListFragment : RainbowCakeFragment<TripListViewState, TripListViewModel>(), NewTripListItemDialogFragment.NewTripListItemDialogListener, NavigationView.OnNavigationItemSelectedListener, AuthChangeDialogFragment.AuthChangeDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentTriplistBinding

    lateinit var tripListPagerAdapter: TripListPagerAdapter
    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapsFragment
    private lateinit var identifierFragment: IdentifierFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTriplistBinding.inflate(layoutInflater, container, false)

        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapsFragment()
        identifierFragment = IdentifierFragment()
        tripListPagerAdapter = TripListPagerAdapter(childFragmentManager, lifecycle, tripsFragment, calendarFragment, mapFragment, identifierFragment)

        binding.fab.setOnClickListener{
            NewTripListItemDialogFragment().show(
                childFragmentManager,
                NewTripListItemDialogFragment.TAG
            )
            context
        }

        tripsFragment.setParent(this)

        binding.mainViewPager.adapter = tripListPagerAdapter
        binding.mainViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) {
                tab, position -> tab.text = when(position) {
                0 -> getString(R.string.list)
                1 -> getString(R.string.calendar)
                2 -> getString(R.string.map)
                3 -> getString(R.string.identifier)
                else -> ""
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setActionBar(binding.toolbar)
        requireActivity().actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener(this)

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById<TextView>(R.id.textViewHeader)
        navUsername.text = viewModel.getUserEmail().toString()
    }

    override fun render(viewState: TripListViewState) {
        when(viewState) {
            is Loading -> {}
            is TripsContent -> {
                tripsFragment.setAdapter()
                calendarFragment.changeCalendar()
                mapFragment.changeMap()
            }
        }.exhaustive
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                viewModel.signOut()
                navigator?.pop()
            }
            R.id.nav_mailchange -> {
                if(isConnected(requireContext()))
                    AuthChangeDialogFragment().show(
                        childFragmentManager,
                        AuthChangeDialogFragment.TAG
                    )
            }
            R.id.nav_passwordchange -> {
                if(isConnected(requireContext())) {
                    viewModel.changePassword()
                    Toast.makeText(context, "Verification email has been sent about your password change", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onTripListItemCreated(newItem: TripListItem) {
        viewModel.add(newItem)
    }

    fun onItemChanged() {
        calendarFragment.changeCalendar()
        mapFragment.changeMap()
    }

    override fun onEmailChanged(password: String?, newEmail: String?) {
        viewModel.changeEmail(requireContext(), password, newEmail)
    }
}*/