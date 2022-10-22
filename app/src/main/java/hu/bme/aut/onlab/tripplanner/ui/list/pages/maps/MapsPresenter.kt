package hu.bme.aut.onlab.tripplanner.ui.list.pages.maps

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import com.google.android.gms.maps.OnMapReadyCallback
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import javax.inject.Inject

class MapsPresenter @Inject constructor(private val tripsInteractor: TripsInteractor) {

    suspend fun setCallback(context: Context): OnMapReadyCallback = withIOContext {
        return@withIOContext tripsInteractor.setCallback(context)
    }
}