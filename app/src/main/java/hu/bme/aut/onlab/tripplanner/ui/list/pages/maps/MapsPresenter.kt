package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapsPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    /*suspend fun setCallback(context: Context): OnMapReadyCallback = withIOContext {
        return@withIOContext tripsInteractor.setCallback(context)
    }*/

    suspend fun addListener(): Flow<List<TripListItem>> = withIOContext {
        tripsInteractor.addListener()
    }

    suspend fun onEditTrip(item: TripListItem) = withIOContext {
        tripsInteractor.editFB(item)
    }
}