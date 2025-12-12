package hu.bme.aut.onlab.tripplanner.views

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dagger.hilt.android.internal.managers.ViewComponentManager
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Composable
fun Maps(
    place: MutableList<String>,
    coordinates: MutableList<LatLng>,
    categories: MutableList<TripListItem.Category>,
    route: List<List<LatLng>>?,
    onMarkerClicked: (LatLng, LatLng, MutableList<LatLng>, MutableList<TripListItem.Category>, MutableList<String>) -> Boolean
) {
    val context = LocalContext.current
    val mContext = if (context is ViewComponentManager.FragmentContextWrapper) context.baseContext
    else context
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastKnownLocation by remember { mutableStateOf<Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val deviceCameraPositionState = rememberCameraPositionState {position = CameraPosition.fromLatLngZoom(deviceLatLng, 20f)}

    val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for(location in locationResult.locations){
                deviceLatLng = LatLng(location.latitude, location.longitude)
            }
        }
    }

    val UPDATE_TIME : Long = 200000

    val locationRequest : LocationRequest = LocationRequest.create().apply{
        interval = UPDATE_TIME
        fastestInterval = UPDATE_TIME/4
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    var permissionGranted = false
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        == PackageManager.PERMISSION_GRANTED
    ) {
        permissionGranted = true
        val locationResult =  fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(mContext as Activity) { t ->
            if (t.isSuccessful) {
                lastKnownLocation = t.result
                if(lastKnownLocation != null)
                    deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                deviceCameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
                Log.i("Location", deviceLatLng.toString())
            } else {
                Log.d("Location", "Current location is null. Using defaults.")
                Log.e("Location", "Exception: %s", t.exception)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(0.dp, 0.dp, 0.dp, 55.dp)
    ) {
        val cameraPositionState: CameraPositionState
        if(!(deviceLatLng.latitude == 0.0 && deviceLatLng.longitude == 0.0)) {
            cameraPositionState = rememberCameraPositionState {
                val startLatLng = deviceLatLng
                position = CameraPosition.fromLatLngZoom(startLatLng, 20f)
            }
        }
        else if(coordinates.isNotEmpty())
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(coordinates[0], 10f)
            }
        else
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(47.4730, 19.0530), 10f)
            }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = permissionGranted)
        ) {
            if (!route.isNullOrEmpty()) {
                route.forEach {
                    Polyline(points = it, color = Color.Red)
                }
            }

            for((index, coordinate) in coordinates.withIndex()) {
                Marker(
                    state = rememberMarkerState(position = coordinate),
                    title = place[index],
                    icon = when(categories[index]) {
                        TripListItem.Category.OUTDOORS -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        TripListItem.Category.BEACHES -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                        TripListItem.Category.SIGHTSEEING -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                        TripListItem.Category.SKIING -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        TripListItem.Category.BUSINESS -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                    },
                    onClick = {onMarkerClicked(coordinate, deviceLatLng, coordinates, categories, place)}
                )
            }
        }
    }
}