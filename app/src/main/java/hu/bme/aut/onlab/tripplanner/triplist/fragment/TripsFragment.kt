package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistDatabase
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTripsBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.TriplistAdapter
import kotlin.concurrent.thread

class TripsFragment : Fragment(), TriplistAdapter.TriplistItemClickListener  {
    private lateinit var binding: FragmentTripsBinding

    private lateinit var database: TriplistDatabase
    private lateinit var adapter: TriplistAdapter

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = TriplistDatabase.getDatabase(requireActivity().getApplicationContext())

        initRecyclerView()
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTripsBinding.inflate(layoutInflater)
        database = TriplistDatabase.getDatabase(requireActivity().getApplicationContext())
        initRecyclerView()
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTripsBinding.inflate(layoutInflater)

        database = TriplistDatabase.getDatabase(requireActivity().getApplicationContext())

        initRecyclerView()
    }*/

    private fun initRecyclerView() {
        adapter = TriplistAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.triplistItemDao().getAll()
            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)
            Log.d("TripsFragment", "TriplistItem update was successful")
        }
    }

    /*override fun onTriplistItemCreated(newItem: TriplistItem) {
        thread {
            database.triplistItemDao().insert(newItem)

            requireActivity().runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }*/

    override fun onItemRemoved(delItem: TriplistItem) {
        thread {
            database.triplistItemDao().deleteItem(delItem)

            requireActivity().runOnUiThread {
                adapter.removeItem(delItem)
            }
        }
    }

    override fun onTripSelected(country: String?, place: String?) {
        /*val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@MainActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_NAME, title)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_DESCRIPTION, description)
        startActivity(showDetailsIntent)*/
    }
}