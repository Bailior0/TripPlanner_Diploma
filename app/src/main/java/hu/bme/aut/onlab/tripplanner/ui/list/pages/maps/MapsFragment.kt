package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.Maps
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class MapsFragment : RainbowCakeFragment<MapsViewState, MapsViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addListener(requireContext())
    }

    override fun render(viewState: MapsViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when(viewState) {
                        is Loading -> {}
                        is TripsContent -> {
                            Maps(
                                viewState.place,
                                viewState.coordinates,
                                viewState.categories,
                                viewState.route,
                                onMarkerClicked = ::onMarkerClicked
                            )
                        }
                    }.exhaustive
                }
            }
        }
    }

    private fun setMap(callback: OnMapReadyCallback) {
        if(isAdded) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun onMarkerClicked(coordinate: LatLng, device: LatLng, coordinates: MutableList<LatLng>, categories: MutableList<TripListItem.Category>, place: MutableList<String>) : Boolean {
        viewModel.onMarkerClicked(coordinate, device, coordinates, categories, place)
        return false
    }
}
