package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistDatabase
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.ActivityTriplistBinding
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTripsBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.TriplistAdapter
import kotlin.concurrent.thread

class TripsFragment : Fragment(), TriplistAdapter.TriplistItemClickListener {
    private lateinit var binding: FragmentTripsBinding

    private lateinit var database: TriplistDatabase
    private lateinit var adapter: TriplistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTripsBinding.inflate(layoutInflater)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        /*adapter = TriplistAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(getContext())
        binding.rvMain.adapter = adapter
        loadItemsInBackground()*/
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.triplistItemDao().getAll()
            /*getActivity()?.runOnUiThread {
                adapter.update(items)
            }*/
        }
    }

    override fun onItemChanged(item: TriplistItem) {
        TODO("Not yet implemented")
    }

    override fun onItemRemoved(item: TriplistItem) {
        TODO("Not yet implemented")
    }

    override fun onTripSelected(name: String?, description: String?) {
        TODO("Not yet implemented")
    }
}