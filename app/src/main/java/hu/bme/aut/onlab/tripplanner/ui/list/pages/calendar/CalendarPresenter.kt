package hu.bme.aut.onlab.tripplanner.ui.list.pages.calendar

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalendarPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    suspend fun addListener(): Flow<List<TripListItem>> = withIOContext {
        tripsInteractor.addListener()
    }
}