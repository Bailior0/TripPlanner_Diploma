package hu.bme.aut.onlab.tripplanner.triplist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.onlab.tripplanner.triplist.fragment.*

class TriplistPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES: Int = 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TripsFragment()
            1 -> CalendarFragment()
            2 -> MapFragment()
            else -> TripsFragment()
        }
    }

    override fun getItemCount(): Int = NUM_PAGES
}