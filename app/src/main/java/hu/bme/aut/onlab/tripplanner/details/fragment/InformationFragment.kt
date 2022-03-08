package hu.bme.aut.onlab.tripplanner.details.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hu.bme.aut.onlab.tripplanner.databinding.FragmentInformationBinding
import hu.bme.aut.onlab.tripplanner.details.DetailsActivity
import hu.bme.aut.onlab.tripplanner.details.data.WeatherDataHolder

class InformationFragment : Fragment() {
    private lateinit var binding: FragmentInformationBinding
    private var weatherDataHolder: WeatherDataHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherDataHolder = if (activity is WeatherDataHolder) {
            activity as WeatherDataHolder?
        } else {
            throw RuntimeException(
                "Activity must implement WeatherDataHolder interface!"
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentInformationBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity as DetailsActivity
        binding.tvCountry.text = act.country
        binding.tvPlace.text = act.place
        binding.tvDescription.text = act.description
        binding.tvDate.text = act.date
        binding.tvCategory.text = act.category
        if(act.visited) {
            binding.tvVisited.text = "Visited"
        }
        else {
            binding.tvVisited.text = "Not yet visited"
        }

        if (weatherDataHolder?.getWeatherData() != null) {
            displayWeatherData()
        }
    }

    private fun displayWeatherData() {
        val weather = weatherDataHolder?.getWeatherData()?.weather?.first()
        val weatherData = weatherDataHolder!!.getWeatherData()
        binding.tvMain.text = weather?.main
        binding.tvWeatherDescription.text = weather?.description
        binding.tvTemperature.text = weatherData?.main?.temp.toString() + " °C"

        Glide.with(this)
            .load("https://openweathermap.org/img/w/${weather?.icon}.png")
            .transition(DrawableTransitionOptions().crossFade())
            .into(binding.ivIcon)
    }
}