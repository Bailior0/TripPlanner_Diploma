package hu.bme.aut.onlab.tripplanner.data.mapdirections

import com.google.android.gms.maps.model.LatLng

data class Route(
    val routePoints: List<List<LatLng>>
)