package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem

@Composable
fun Maps(
    place: MutableList<String>,
    coordinates: MutableList<LatLng>,
    categories: MutableList<TripListItem.Category>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(0.dp, 0.dp, 0.dp, 55.dp)
    ) {
        val cameraPositionState: CameraPositionState
        if(coordinates.isNotEmpty())
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(coordinates[0], 10f)
            }
        else
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
            }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            for((index, coordinate) in coordinates.withIndex()) {
                Marker(
                    state = MarkerState(position = coordinate),
                    title = place[index],
                    icon = when(categories[index]) {
                        TripListItem.Category.OUTDOORS -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        TripListItem.Category.BEACHES -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                        TripListItem.Category.SIGHTSEEING -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                        TripListItem.Category.SKIING -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        TripListItem.Category.BUSINESS -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                    }
                )
            }
        }
    }
}