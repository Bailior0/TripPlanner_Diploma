package hu.bme.aut.onlab.tripplanner.viewmodels

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.Loading
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsContent
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsPresenter
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TripsViewModelTest : ViewModelTest() {

    private lateinit var tripsPresenter: TripsPresenter
    private lateinit var viewModel: TripsViewModel

    @Before
    fun initEach() {
        tripsPresenter = mockk()
        viewModel = TripsViewModel(tripsPresenter)
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
    fun `addListener collects trips and updates state`() = runTest {
        coEvery { tripsPresenter.addListener() } returns flowOf(listOf(MOCK_TRIP))

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.addListener()
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                TripsContent(loading = true),
                TripsContent(trips = listOf(MOCK_TRIP), loading = false)
            )
        }
    }

    @Test
    fun `addFB delegates to presenter`() = runTest {
        coEvery { tripsPresenter.addFB(MOCK_TRIP) } returns Unit

        viewModel.addFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsPresenter.addFB(MOCK_TRIP) }
    }

    @Test
    fun `editFB delegates to presenter and sets loading state`() = runTest {
        coEvery { tripsPresenter.editFB(MOCK_TRIP) } returns Unit

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.editFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsPresenter.editFB(MOCK_TRIP) }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(TripsContent(loading = true))
        }
    }

    @Test
    fun `deleteFB delegates to presenter`() = runTest {
        coEvery { tripsPresenter.removeFB(MOCK_TRIP) } returns Unit

        viewModel.deleteFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsPresenter.removeFB(MOCK_TRIP) }
    }
}
