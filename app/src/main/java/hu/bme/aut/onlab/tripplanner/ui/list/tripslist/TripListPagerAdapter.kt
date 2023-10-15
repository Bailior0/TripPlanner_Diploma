package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.maps.MapsFragment
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsFragment

class TripListPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val tripsFragment: TripsFragment, private val calendarFragment: CalendarFragment, private val mapFragment: MapsFragment, private val identifierFragment: IdentifierFragment) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val NUM_PAGES: Int = 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> tripsFragment
            1 -> calendarFragment
            2 -> mapFragment
            3 -> identifierFragment
            else -> tripsFragment
        }
    }

    override fun getItemCount(): Int = NUM_PAGES
}