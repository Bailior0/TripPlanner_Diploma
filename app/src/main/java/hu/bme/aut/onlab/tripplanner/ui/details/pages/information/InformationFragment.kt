package hu.bme.aut.onlab.tripplanner.ui.details.pages.information

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.Information
import hu.bme.aut.onlab.tripplanner.views.helpers.FullScreenLoading
import hu.bme.aut.onlab.tripplanner.views.theme.AppJustUi1Theme

@AndroidEntryPoint
class InformationFragment : RainbowCakeFragment<InformationViewState, InformationViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var trip: TripListItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenLoading()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTrip(trip)
        viewModel.setWeather(trip, trip.place, requireContext())
    }

    fun setTrip(trip: TripListItem) {
        this.trip = trip
    }

    override fun render(viewState: InformationViewState) {
        (view as ComposeView).setContent {
            AppJustUi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when(viewState) {
                        is Loading -> {}
                        is DetailsContent -> Information(viewState.trip, null)
                        is WeatherContent -> Information(viewState.trip, viewState.weather)
                    }.exhaustive
                }
            }
        }
    }
}
