package hu.bme.aut.onlab.tripplanner.triplist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.*
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.*
import hu.bme.aut.onlab.tripplanner.triplist.fragment.*
import kotlin.concurrent.thread

class TriplistActivity : AppCompatActivity(), NewTriplistItemDialogFragment.NewTriplistItemDialogListener {
    private lateinit var binding: ActivityTriplistBinding

    private lateinit var database: TriplistDatabase
    private lateinit var triplistPagerAdapter: TriplistPagerAdapter
    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriplistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapFragment()
        triplistPagerAdapter = TriplistPagerAdapter(this, tripsFragment, calendarFragment, mapFragment)

        database = TriplistDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener{
            NewTriplistItemDialogFragment().show(
                supportFragmentManager,
                NewTriplistItemDialogFragment.TAG
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mainViewPager.adapter = triplistPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) {
                tab, position -> tab.text = when(position) {
                0 -> getString(R.string.list)
                1 -> getString(R.string.calendar)
                2 -> getString(R.string.map)
                else -> ""
            }
        }.attach()
    }

    override fun onTriplistItemCreated(newItem: TriplistItem) {
        thread {
            val newId = database.triplistItemDao().insert(newItem)
            val newTripItem = newItem.copy(
                id = newId
            )

            runOnUiThread {
                tripsFragment.triplistItemCreated(newTripItem)
            }
        }
    }

    override fun onTriplistItemEdited(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)

            runOnUiThread {
                tripsFragment.triplistItemEdited(item)
            }
        }
    }
}