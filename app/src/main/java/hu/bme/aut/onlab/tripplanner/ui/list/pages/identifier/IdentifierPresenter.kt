package hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier

import android.content.Context
import android.graphics.Bitmap
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import javax.inject.Inject

class IdentifierPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    suspend fun identify(image: Bitmap, context: Context): String = withIOContext {
        return@withIOContext tripsInteractor.identify(image, context)
    }
}