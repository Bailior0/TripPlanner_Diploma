package hu.bme.aut.onlab.tripplanner.viewmodels

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarContent
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarPresenter
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.CalendarViewModel
import hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar.Loading
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest : ViewModelTest() {

    private lateinit var presenter: CalendarPresenter
    private lateinit var viewModel: CalendarViewModel

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
        presenter = mockk()
        viewModel = CalendarViewModel(presenter)
    }

    @Test
    fun `addListener emits Loading then CalendarContent with trips`() = runTest {
        coEvery { presenter.addListener() } returns flowOf(listOf(mockTrip))

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.addListener()
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                CalendarContent(loading = true),
                CalendarContent(trips = listOf(mockTrip), loading = false)
            )
        }
    }
}
