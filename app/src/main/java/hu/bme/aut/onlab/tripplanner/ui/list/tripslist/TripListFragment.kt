package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import co.zsmb.rainbowcake.navigation.navigator
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTriplistBinding
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.authchange.AuthChangeDialogFragment
import hu.bme.aut.onlab.tripplanner.ui.list.dialogs.newitem.NewTripListItemDialogFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.maps.MapsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsFragment

@AndroidEntryPoint
class TripListFragment : RainbowCakeFragment<TripListViewState, TripListViewModel>(), NewTripListItemDialogFragment.NewTripListItemDialogListener, NavigationView.OnNavigationItemSelectedListener, AuthChangeDialogFragment.AuthChangeDialogListener {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentTriplistBinding

    lateinit var tripListPagerAdapter: TripListPagerAdapter
    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTriplistBinding.inflate(layoutInflater, container, false)

        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapsFragment()
        tripListPagerAdapter = TripListPagerAdapter(childFragmentManager, lifecycle, tripsFragment, calendarFragment, mapFragment)

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
                tripsFragment.setAdapter(viewState.trips)
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
}