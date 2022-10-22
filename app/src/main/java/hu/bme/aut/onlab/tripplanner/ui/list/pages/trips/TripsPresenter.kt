package hu.bme.aut.onlab.tripplanner.ui.list.pages.trips

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import javax.inject.Inject

class TripsPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    suspend fun load(): List<TripListItem> = withIOContext {
        return@withIOContext tripsInteractor.load()
    }

    suspend fun edit(editedItem: TripListItem): List<TripListItem> = withIOContext {
        return@withIOContext tripsInteractor.edit(editedItem)
    }

    suspend fun remove(removedItem: TripListItem): List<TripListItem> = withIOContext {
        return@withIOContext tripsInteractor.remove(removedItem)
    }
}