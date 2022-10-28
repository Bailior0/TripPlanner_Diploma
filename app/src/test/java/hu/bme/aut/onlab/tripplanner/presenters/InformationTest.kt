package hu.bme.aut.onlab.tripplanner.presenters

import android.content.Context
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData
import hu.bme.aut.onlab.tripplanner.domain.DetailsInteractor
import hu.bme.aut.onlab.tripplanner.ui.details.pages.information.InformationPresenter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InformationPresenterTest : PresenterTest() {
    private lateinit var detailsInteractor: DetailsInteractor
    private lateinit var presenter: InformationPresenter

    @Before
    fun initEach() {
        detailsInteractor = mockk()
        presenter = InformationPresenter(detailsInteractor)
    }

    companion object {
        private val MOCK_WEATHER = WeatherData()
        private val MOCK_CONTEXT = mockk<Context>(relaxed = true)
    }

    @Test
    fun informationGetItemsTest() = runBlocking {
        coEvery { detailsInteractor.getWeather("Paris", MOCK_CONTEXT) } returns MOCK_WEATHER

        val weather = presenter.getWeather("Paris", MOCK_CONTEXT)

        Truth.assertThat(weather).isEqualTo(MOCK_WEATHER)
    }
}