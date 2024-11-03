package hu.bme.aut.onlab.tripplanner.data.mapdirections

import com.google.android.gms.maps.model.LatLng
import javax.inject.Named

interface MapsRepository {
    suspend fun getDirections(
        origin: LatLng,
        destination: LatLng
    ): Resource<Route>
}