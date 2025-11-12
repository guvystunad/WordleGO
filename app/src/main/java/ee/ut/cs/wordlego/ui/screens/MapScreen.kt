package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import ee.ut.cs.wordlego.ui.components.BackButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlin.math.*
import androidx.compose.ui.graphics.Color

data class WordlePoint(
    val name: String,
    val position: LatLng
)
@Composable
fun MapScreen(
    navController: NavHostController,
    onLocationSelected: (String) -> Unit,
    userLocation: LatLng?
) {
    //Default camera position centered on Tartu
    val tartu = LatLng(58.3780, 26.7290)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tartu, 13f)
    }
    //if user's location is known zoom in on them
    userLocation?.let {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
    }
    //list of map points with names and coordinates
    val wordleLocations = listOf(
        WordlePoint(
            name = "delta",
            position = LatLng(58.385059, 26.725106)
        ),
        WordlePoint(
            name = "peahoone",
            position = LatLng(58.380871617586784, 26.71993282910922)
        )
    )


    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map composable
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            //Iterate through map points
            wordleLocations.forEach { point ->

                val location = point.position
                //calvulate the distance from user to the marker
                val distanceMeters = userLocation?.let { haversineDistance(it, location) } ?: Double.MAX_VALUE
                val isClose = distanceMeters < 300

                // change marker colour when user is close
                val hue = if (isClose) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_ORANGE

                //place marker on the map
                Marker(
                    state = MarkerState(position = point.position),
                    title = point.name,
                    icon = BitmapDescriptorFactory.defaultMarker(hue),
                    onClick = {
                        // allow player to select location only when close
                        if (isClose) {
                            onLocationSelected(point.name)   // <-- SIIT LÄHEB “delta”
                        }
                        true
                    }
                )

                if (isClose) {
                    Circle(
                        center = location,
                        radius = 40.0,
                        strokeColor = Color(0x80ffe31e),
                        fillColor = Color(0x40ffff8b),
                        strokeWidth = 2f
                    )
                }
            }

        }
        // back button over the map
        BackButton(
            navController = navController,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .zIndex(1f)
        )
    }
}

/**
 * Compute distance between two LatLng points (in meters) using Haversine formula
 */
fun haversineDistance(start: LatLng, end: LatLng): Double {
    val earthRadius = 6371000.0 // meters
    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLon = Math.toRadians(end.longitude - start.longitude)
    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(start.latitude)) *
            cos(Math.toRadians(end.latitude)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}