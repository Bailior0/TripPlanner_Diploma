package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.database.TripListItemDao
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker
import java.io.IOException
import java.util.*
import javax.inject.Inject

class TripsInteractor @Inject constructor(private val database: TripListItemDao) {

    suspend fun load(): List<TripListItem> {
        val data  = mutableListOf<TripListItem>()
        data.addAll(database.getAll())
        data.sortBy { it.date }
        return data
    }

    suspend fun add(newItem: TripListItem): List<TripListItem> {
        database.insert(newItem)
        return load()
    }

    suspend fun edit(editedItem: TripListItem): List<TripListItem> {
        database.update(editedItem)
        return load()
    }

    suspend fun remove(removedItem: TripListItem): List<TripListItem> {
        database.delete(removedItem)
        return load()
    }

    suspend fun setCallback(context: Context): OnMapReadyCallback {
        val items = load()
        return OnMapReadyCallback { googleMap ->
            googleMap.clear()
            val connected = ConnectivityChecker.isConnected(context)
            val geocoder = Geocoder(context, Locale.getDefault())
            val bld = LatLngBounds.Builder()
            var matches: MutableList<Address>? = null
            if(connected) {
                for (item in items) {
                    try {
                        matches = geocoder.getFromLocationName(item.place + " " + item.country, 1)
                    } catch(e: IOException) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    var coordinates: LatLng

                    if (!matches.isNullOrEmpty() && matches.size > 0) {
                        coordinates = LatLng(matches[0].latitude, matches[0].longitude)
                        when(item.category) {
                            TripListItem.Category.OUTDOORS -> googleMap.addMarker(
                                MarkerOptions().position(coordinates).title(item.place).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                            TripListItem.Category.BEACHES -> googleMap.addMarker(
                                MarkerOptions().position(coordinates).title(item.place).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
                            TripListItem.Category.SIGHTSEEING -> googleMap.addMarker(
                                MarkerOptions().position(coordinates).title(item.place).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                            TripListItem.Category.SKIING -> googleMap.addMarker(
                                MarkerOptions().position(coordinates).title(item.place).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                            TripListItem.Category.BUSINESS -> googleMap.addMarker(
                                MarkerOptions().position(coordinates).title(item.place).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                        }
                        bld.include(coordinates)
                    }
                }
            }
            if (items.isNotEmpty() && connected && !matches.isNullOrEmpty()) {
                val bounds = bld.build()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1000,1000, 70))
            }
            googleMap.setMapStyle(MapStyleOptions(context.getString(R.string.style_json)))
        }
    }
}