package hu.bme.aut.onlab.tripplanner.presenters

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsPresenter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TripListTestPresenterTest : PresenterTest() {
    private lateinit var tripsInteractor: TripsInteractor
    private lateinit var presenter: TripsPresenter

    @Before
    fun initEach() {
        tripsInteractor = mockk()
        presenter = TripsPresenter(tripsInteractor)
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
    }

    @Test
    fun tripsLoadedTest() = runBlocking {
        coEvery { tripsInteractor.load() } returns listOf(MOCK_TRIP)

        val items = presenter.load()

        Truth.assertThat(items).isEqualTo(listOf(MOCK_TRIP))
    }

    @Test
    fun tripsEditedTest() = runBlocking {
        coEvery { tripsInteractor.edit(MOCK_TRIP) } returns listOf(MOCK_TRIP)

        val items = presenter.edit(MOCK_TRIP)

        Truth.assertThat(items).isEqualTo(listOf(MOCK_TRIP))
    }

    @Test
    fun tripsRemovedTest() = runBlocking {
        coEvery { tripsInteractor.remove(MOCK_TRIP) } returns listOf(MOCK_TRIP)

        val items = presenter.remove(MOCK_TRIP)

        Truth.assertThat(items).isEqualTo(listOf(MOCK_TRIP))
    }
}