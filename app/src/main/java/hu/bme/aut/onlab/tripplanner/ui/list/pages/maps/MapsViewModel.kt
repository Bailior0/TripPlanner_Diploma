package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.mapdirections.MapsRepository
import hu.bme.aut.onlab.tripplanner.data.mapdirections.Resource
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsPresenter: MapsPresenter, private val mapsRepository: MapsRepository) : RainbowCakeViewModel<MapsViewState>(
    Loading
) {

    fun addListener(context: Context) = execute {
        viewModelScope.launch {
            mapsPresenter.addListener().collect {
                val items = it
                val connected = ConnectivityChecker.isConnected(context)
                val geocoder = Geocoder(context, Locale.getDefault())
                var matches: MutableList<Address>? = null
                val coordinates: MutableList<LatLng> = mutableListOf()
                val categories: MutableList<TripListItem.Category> = mutableListOf()
                val place: MutableList<String> = mutableListOf()
                if (connected) {
                    for (item in items) {
                        matches = null
                        var coordinate: LatLng?

                        if (item.coordinateX == "" || item.coordinateY == "" || item.coordinateX == null || item.coordinateY == null) {
                            try {
                                matches = geocoder.getFromLocationName(
                                    item.place + " " + item.country,
                                    1
                                )
                            } catch (e: IOException) {
                                Toast.makeText(
                                    context,
                                    e.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            coordinate = LatLng(
                                item.coordinateX.toDouble(),
                                item.coordinateY.toDouble()
                            )
                            coordinates.add(coordinate)
                            categories.add(item.category)
                            place.add(item.place)
                        }

                        if (!matches.isNullOrEmpty() && matches.size > 0) {
                            coordinate =
                                LatLng(matches[0].latitude, matches[0].longitude)
                            item.coordinateX = matches[0].latitude.toString()
                            item.coordinateY = matches[0].longitude.toString()
                            coordinates.add(coordinate)
                            categories.add(item.category)
                            place.add(item.place)
                            mapsPresenter.onEditTrip(item)
                        }
                    }
                }

                viewState = TripsContent(loading = true)
                viewState = TripsContent(
                    coordinates = coordinates,
                    categories = categories,
                    place = place,
                    route = null,
                    loading = false
                )
            }
        }
    }

    fun onMarkerClicked(coordinate: LatLng, device: LatLng, coordinates: MutableList<LatLng>, categories: MutableList<TripListItem.Category>, place: MutableList<String>) = execute {
        val pathResult = mapsRepository.getDirections(
            origin = device,
            destination = coordinate
        )

        var route: List<List<LatLng>>? = null
        when (pathResult) {
            is Resource.Error -> {}
            is Resource.Success -> {
                route = pathResult.data?.routePoints
            }
        }

        viewState = TripsContent(loading = true)
        viewState = TripsContent(
            coordinates = coordinates,
            categories = categories,
            place = place,
            route = route,
            loading = false
        )
    }
}