package hu.bme.aut.onlab.tripplanner.triplist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.onlab.tripplanner.MainActivity
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.*
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.*
import hu.bme.aut.onlab.tripplanner.triplist.fragment.*
import kotlinx.android.synthetic.main.nav_header_triplist.*
import kotlinx.android.synthetic.main.nav_header_triplist.view.*
import kotlin.concurrent.thread

class TriplistActivity : AppCompatActivity(), NewTriplistItemDialogFragment.NewTriplistItemDialogListener, NavigationView.OnNavigationItemSelectedListener {
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener(this)

        triplistPagerAdapter = TriplistPagerAdapter(this, tripsFragment, calendarFragment, mapFragment)

        database = TriplistDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener{
            NewTriplistItemDialogFragment().show(
                supportFragmentManager,
                NewTriplistItemDialogFragment.TAG
            )
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById<TextView>(R.id.textViewHeader)
        navUsername.text = FirebaseAuth.getInstance().currentUser?.email.toString()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}