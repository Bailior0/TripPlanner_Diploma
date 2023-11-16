package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates[0], 10f)
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
            /*Marker(
                state = MarkerState(position = coordinates[0]),
                title = place[0],
                snippet = "Marker in Singapore"
            )*/
        }
    }
}