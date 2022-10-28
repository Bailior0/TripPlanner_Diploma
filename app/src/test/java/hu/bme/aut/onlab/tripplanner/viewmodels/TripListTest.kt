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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TripListViewModelTest : ViewModelTest() {
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
    fun tripsLoadedTest() = runTest {
        coEvery { tripsPresenter.load() } returns listOf(MOCK_TRIP)

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.load()
        coVerify(exactly = 1) { tripsPresenter.load() }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(TripsContent(trips = listOf(MOCK_TRIP), loading = false))
        }
    }

    @Test
    fun tripsEditedTest() = runTest {
        coEvery { tripsPresenter.edit(MOCK_TRIP) } returns listOf(MOCK_TRIP)

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.edit(MOCK_TRIP)
        coVerify(exactly = 1) { tripsPresenter.edit(MOCK_TRIP) }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(TripsContent(trips = listOf(MOCK_TRIP), loading = false))
        }
    }

    @Test
    fun tripsRemovedTest() = runTest {
        coEvery { tripsPresenter.remove(MOCK_TRIP) } returns listOf(MOCK_TRIP)

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.remove(MOCK_TRIP)
        coVerify(exactly = 1) { tripsPresenter.remove(MOCK_TRIP) }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(TripsContent(trips = listOf(MOCK_TRIP), loading = true))
        }
    }
}