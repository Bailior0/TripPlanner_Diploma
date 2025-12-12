package hu.bme.aut.onlab.tripplanner.presenters

import co.zsmb.rainbowcake.test.base.PresenterTest
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import hu.bme.aut.onlab.tripplanner.ui.list.pages.trips.TripsPresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TripsPresenterTest : PresenterTest() {

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
    fun `addListener returns the flow from TripsInteractor`() = runBlocking {
        val flowTrips = flowOf(listOf(MOCK_TRIP))
        coEvery { tripsInteractor.addListener() } returns flowTrips

        val result = presenter.addListener()

        assertEquals(flowTrips, result)
    }

    @Test
    fun `addFB delegates to TripsInteractor_addTrip`() = runBlocking {
        coEvery { tripsInteractor.addTrip(MOCK_TRIP) } returns Unit

        presenter.addFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsInteractor.addTrip(MOCK_TRIP) }
    }

    @Test
    fun `editFB delegates to TripsInteractor_editTrip`() = runBlocking {
        coEvery { tripsInteractor.editTrip(MOCK_TRIP) } returns Unit

        presenter.editFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsInteractor.editTrip(MOCK_TRIP) }
    }

    @Test
    fun `removeFB delegates to TripsInteractor_removeTrip`() = runBlocking {
        coEvery { tripsInteractor.removeTrip(MOCK_TRIP) } returns Unit

        presenter.removeFB(MOCK_TRIP)

        coVerify(exactly = 1) { tripsInteractor.removeTrip(MOCK_TRIP) }
    }
}
