package hu.bme.aut.onlab.tripplanner.presenters

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarPresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarPresenterTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)

    private lateinit var tripsInteractor: TripsInteractor
    private lateinit var presenter: CalendarPresenter

    private val mockTrip = TripListItem(
        place = "Paris",
        country = "France",
        description = "",
        date = "2025.01.01.",
        category = TripListItem.Category.SIGHTSEEING,
        visited = true
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        tripsInteractor = mockk()
        presenter = CalendarPresenter(tripsInteractor)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `addListener delegates to TripsInteractor`() = runTest {
        val flowTrips = flowOf(listOf(mockTrip))
        coEvery { tripsInteractor.addListener() } returns flowTrips

        val result = presenter.addListener()

        assertThat(result).isEqualTo(flowTrips)
        coVerify(exactly = 1) { tripsInteractor.addListener() }
    }
}
