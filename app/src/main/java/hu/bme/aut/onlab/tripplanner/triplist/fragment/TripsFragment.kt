package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.onlab.tripplanner.data.TriplistDatabase
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTripsBinding
import hu.bme.aut.onlab.tripplanner.details.DetailsActivity
import hu.bme.aut.onlab.tripplanner.triplist.TriplistActivity
import hu.bme.aut.onlab.tripplanner.triplist.adapter.TriplistAdapter
import kotlin.concurrent.thread

class TripsFragment : Fragment(), TriplistAdapter.TriplistItemClickListener {
    private lateinit var binding: FragmentTripsBinding

    private lateinit var database: TriplistDatabase
    private lateinit var adapter: TriplistAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTripsBinding.inflate(layoutInflater, container, false)
        database = TriplistDatabase.getDatabase(requireActivity().applicationContext)

        initRecyclerView()
        return binding.root
    }

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
                adapter.updateItem(items)
            }
        }
    }

    override fun onItemChanged(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)

            requireActivity().runOnUiThread {
                adapter.editItem(item)
            }
        }
    }

    override fun onItemRemoved(delItem: TriplistItem) {
        thread {
            database.triplistItemDao().deleteItem(delItem)

            requireActivity().runOnUiThread {
                adapter.removeItem(delItem)
                val act = requireActivity() as TriplistActivity
                act.deleteItem()
            }
        }
    }

    override fun onItemEdited(editItem: TriplistItem) {
        NewTriplistItemDialogFragment(editItem).show(
            childFragmentManager,
            NewTriplistItemDialogFragment.TAG
        )
    }

    override fun onTripSelected(country: String?, place: String?, description: String?, date: String?, category: String?, visited: Boolean) {
        val showDetailsIntent = Intent()
        showDetailsIntent.setClass(requireActivity().applicationContext, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_COUNTRY, country)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_PLACE, place)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_DESCRIPTION, description)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_DATE, date)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_CATEGORY, category)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_VISITED, visited)
        startActivity(showDetailsIntent)
    }

    fun triplistItemCreated(newItem: TriplistItem) {
        if(isAdded) {
            requireActivity().runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    fun triplistItemEdited(item: TriplistItem) {
        requireActivity().runOnUiThread {
            adapter.editItem(item)
        }
    }
}