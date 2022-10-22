package hu.bme.aut.onlab.tripplanner.ui.details.pages.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import hu.bme.aut.onlab.tripplanner.databinding.FragmentInformationBinding

@AndroidEntryPoint
class InformationFragment : RainbowCakeFragment<InformationViewState, InformationViewModel>() {
    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var binding: FragmentInformationBinding
    private lateinit var trip: TripListItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInformationBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTrip(trip)
        viewModel.setWeather(trip.place, requireContext())
    }

    fun setTrip(trip: TripListItem) {
        this.trip = trip
    }

    override fun render(viewState: InformationViewState) {
        when(viewState) {
            is Loading -> {}
            is DetailsContent -> {
                binding.tvCountry.text = viewState.trip?.country
                binding.tvPlace.text = viewState.trip?.place
                binding.tvDescription.text = viewState.trip?.description
                binding.tvDate.text = viewState.trip?.date
                binding.tvCategory.text = viewState.trip?.category.toString()
                if(viewState.trip?.visited == true) {
                    binding.tvVisited.setText(R.string.visited)
                }
                else {
                    binding.tvVisited.setText(R.string.not_visited)
                }
            }
            is WeatherContent -> { displayWeatherData(viewState.weather) }
        }.exhaustive
    }

    private fun displayWeatherData(weatherList: WeatherData?) {
        if(weatherList != null) {
            val weather = weatherList.weather?.first()
            binding.tvMain.text = weather?.main
            binding.tvWeatherDescription.text = weather?.description
            val temp = weatherList.main?.temp.toString() + " °C"
            binding.tvTemperature.text = temp

            Glide.with(this)
                .load("https://openweathermap.org/img/w/${weather?.icon}.png")
                .transition(DrawableTransitionOptions().crossFade())
                .into(binding.ivIcon)
        }
    }
}
