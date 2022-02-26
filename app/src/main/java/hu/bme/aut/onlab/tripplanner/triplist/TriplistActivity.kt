package hu.bme.aut.onlab.tripplanner.triplist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.*
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.*
import hu.bme.aut.onlab.tripplanner.triplist.fragment.*
import kotlin.concurrent.thread

class TriplistActivity : AppCompatActivity(), TriplistAdapter.TriplistItemClickListener, NewTriplistItemDialogFragment.NewTriplistItemDialogListener {
    private lateinit var binding: ActivityTriplistBinding

    private lateinit var database: TriplistDatabase
    private lateinit var adapter: TriplistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriplistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = TriplistDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener{
            NewTriplistItemDialogFragment().show(
                supportFragmentManager,
                NewTriplistItemDialogFragment.TAG
            )
        }

        //initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        val triplistPagerAdapter = TriplistPagerAdapter(this)
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

    /*private fun initRecyclerView() {
        adapter = TriplistAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }*/

    /*private fun loadItemsInBackground() {
        thread {
            val items = database.triplistItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }*/

    override fun onItemChanged(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)
            Log.d("TriplistActivity", "TriplistItem update was successful")
        }
    }

    override fun onTriplistItemCreated(newItem: TriplistItem) {
        thread {
            database.triplistItemDao().insert(newItem)

            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onItemRemoved(delItem: TriplistItem) {
        thread {
            database.triplistItemDao().deleteItem(delItem)

            runOnUiThread {
                adapter.removeItem(delItem)
            }
        }
    }

    override fun onTripSelected(title: String?, description: String?) {
        /*val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@MainActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_NAME, title)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_DESCRIPTION, description)
        startActivity(showDetailsIntent)*/
    }
}