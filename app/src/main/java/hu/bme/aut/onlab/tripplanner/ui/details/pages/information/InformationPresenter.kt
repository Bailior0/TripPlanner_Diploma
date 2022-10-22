package hu.bme.aut.onlab.tripplanner.ui.details.pages.information

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import hu.bme.aut.onlab.tripplanner.domain.DetailsInteractor
import javax.inject.Inject

class InformationPresenter @Inject constructor(private val detailsInteractor: DetailsInteractor) {

    suspend fun getWeather(city: String, context: Context): WeatherData? = withIOContext {
        return@withIOContext detailsInteractor.getWeather(city, context)
    }
}