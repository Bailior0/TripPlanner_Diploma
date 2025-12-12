package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TripsPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    suspend fun addListener(): Flow<List<TripListItem>> = withIOContext {
        tripsInteractor.addListener()
    }

    suspend fun addFB(newItem: TripListItem) = withIOContext {
        tripsInteractor.addTrip(newItem)
    }

    suspend fun editFB(editedItem: TripListItem) = withIOContext {
        tripsInteractor.editTrip(editedItem)
    }

    suspend fun removeFB(removedItem: TripListItem) = withIOContext {
        tripsInteractor.removeTrip(removedItem)
    }
}