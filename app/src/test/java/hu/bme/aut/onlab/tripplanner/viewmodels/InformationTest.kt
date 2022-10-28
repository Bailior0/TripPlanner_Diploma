package hu.bme.aut.onlab.tripplanner.viewmodels

import android.content.Context
import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import hu.bme.aut.onlab.tripplanner.ui.details.pages.information.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InformationViewModelTest : ViewModelTest() {
    private lateinit var informationPresenter: InformationPresenter
    private lateinit var viewModel: InformationViewModel

    @Before
    fun initEach() {
        informationPresenter = mockk()
        viewModel = InformationViewModel(informationPresenter)
    }

    companion object {
        private val MOCK_TRIP = TripListItem(
            place = "Paris",
            country = "France",
            description = "",
            date = "2022.01.01.",
            category = TripListItem.Category.SIGHTSEEING,
            visited = true
        )
        private val MOCK_WEATHER = WeatherData()
        private val MOCK_CONTEXT = mockk<Context>(relaxed = true)
    }

    @Test
    fun informationTripTest() = runTest {
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.setTrip(MOCK_TRIP)
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(DetailsContent(false, MOCK_TRIP))
        }
    }

    @Test
    fun informationWeatherTest() = runTest {
        coEvery { informationPresenter.getWeather("Paris", MOCK_CONTEXT) } returns MOCK_WEATHER

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.setWeather(MOCK_TRIP, "Paris", MOCK_CONTEXT)
        coVerify(exactly = 1) { informationPresenter.getWeather("Paris", MOCK_CONTEXT) }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(WeatherContent(false, MOCK_TRIP, MOCK_WEATHER))
        }
    }
}