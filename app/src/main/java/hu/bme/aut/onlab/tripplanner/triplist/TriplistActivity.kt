package hu.bme.aut.onlab.tripplanner.triplist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.onlab.tripplanner.BaseActivity
import hu.bme.aut.onlab.tripplanner.MainActivity
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.*
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.*
import hu.bme.aut.onlab.tripplanner.triplist.fragment.*
import kotlin.concurrent.thread

class TriplistActivity : BaseActivity(), NewTriplistItemDialogFragment.NewTriplistItemDialogListener, NavigationView.OnNavigationItemSelectedListener, AuthChangeDialogFragment.AuthChangeDialogListener {
    private lateinit var binding: ActivityTriplistBinding

    private lateinit var database: TriplistDatabase
    private lateinit var triplistPagerAdapter: TriplistPagerAdapter
    private lateinit var tripsFragment: TripsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var mapFragment: MapsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriplistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tripsFragment = TripsFragment()
        calendarFragment = CalendarFragment()
        mapFragment = MapsFragment()
        triplistPagerAdapter = TriplistPagerAdapter(this, tripsFragment, calendarFragment, mapFragment)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener(this)

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
        binding.mainViewPager.isUserInputEnabled = false

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
                mapFragment.setMarkers()
            }
        }

    }

    override fun onTriplistItemEdited(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)

            runOnUiThread {
                tripsFragment.triplistItemEdited(item)
                mapFragment.setMarkers()
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
            R.id.nav_mailchange -> {
                AuthChangeDialogFragment().show(
                    supportFragmentManager,
                    AuthChangeDialogFragment.TAG
                )
            }
            R.id.nav_passwordchange -> {
                val mail = FirebaseAuth.getInstance().currentUser?.email as String
                FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                toast("Verification email has been sent about your password change")
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

    fun deleteItem() {
        runOnUiThread {
            mapFragment.setMarkers()
        }
    }

    override fun onEmailChanged(password: String?, newEmail: String?) {
        if(password.isNullOrEmpty()){
            toast("Please enter a valid password")
        }
        else if(newEmail.isNullOrEmpty() || !newEmail.contains('@')){
            toast("Please enter a valid new email")
        }
        else {
            val mail = FirebaseAuth.getInstance().currentUser?.email as String
            val credential = EmailAuthProvider.getCredential (
                mail,
                password
            )

            FirebaseAuth.getInstance().currentUser?.reauthenticate(credential)
                ?.addOnSuccessListener {
                    hideProgressDialog()
                    FirebaseAuth.getInstance().currentUser?.verifyBeforeUpdateEmail(newEmail)

                    toast("Verification email has been sent")
                }
                ?.addOnFailureListener { exception ->
                    hideProgressDialog()

                    toast(exception.localizedMessage)
                }
        }
    }
}