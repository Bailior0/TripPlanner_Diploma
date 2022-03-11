package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.*
import hu.bme.aut.onlab.tripplanner.databinding.FragmentMapsBinding
import java.util.*
import kotlin.concurrent.thread

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var database: TriplistDatabase
    private lateinit var items: List<TriplistItem>

    private lateinit var callback: OnMapReadyCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMarkers()
    }

    fun setMarkers() {
        if(isAdded) {
            database = TriplistDatabase.getDatabase(requireActivity().applicationContext)
            items = emptyList()
            thread {
                items = database.triplistItemDao().getAll()
                database.close()
                requireActivity().runOnUiThread {
                    setCallback()
                    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment?.getMapAsync(callback)
                }
            }
        }
    }

    private fun setCallback() {
        callback = OnMapReadyCallback { googleMap ->
            googleMap.clear()
            val geocoder = Geocoder(requireActivity().applicationContext, Locale.getDefault())
            val bld = LatLngBounds.Builder()
            for (item in items) {
                val matches = geocoder.getFromLocationName(item.place + " " + item.country, 1)
                var coordinates: LatLng

                if (matches.size > 0) {
                    coordinates = LatLng(matches[0].latitude, matches[0].longitude)
                    googleMap.addMarker(MarkerOptions().position(coordinates).title(item.place))
                    bld.include(coordinates)
                }
            }
            if (items.isNotEmpty()) {
                val bounds = bld.build()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1000,1000, 70))
            }
            googleMap.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))
        }
    }
}