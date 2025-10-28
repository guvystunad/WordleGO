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

@Composable
fun MapScreen(
    navController: NavHostController,
    onLocationSelected: (LatLng) -> Unit,
    userLocation: LatLng?
) {
    val tartu = LatLng(58.3780, 26.7290)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tartu, 13f)
    }

    userLocation?.let {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
    }

    val wordleLocations = listOf(
        LatLng(58.3780, 26.7290),
        LatLng(58.3850, 26.7200),
        LatLng(58.3720, 26.7400)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            wordleLocations.forEach { location ->
                val distanceMeters = userLocation?.let { haversineDistance(it, location) } ?: Double.MAX_VALUE

                // Define proximity thresholds
                val isClose = distanceMeters < 300 // within 300 meters → bigger marker

                // Adjust color and scale based on distance
                val hue = BitmapDescriptorFactory.HUE_ORANGE
                val scale = if (isClose) 1.3f else 0.8f

                Marker(
                    state = MarkerState(position = location),
                    title = "Wordle Location",
                    icon = BitmapDescriptorFactory.defaultMarker(hue),
                    onClick = {
                        onLocationSelected(location)
                        true
                    },
                    alpha = 1f,
                    // Trick: use `anchor` + scale control (Compose Map doesn’t support size directly)
                )

                // Optional: could add a circle overlay around close markers
                if (isClose) {
                    Circle(
                        center = location,
                        radius = 40.0, // small highlight
                        strokeColor = Color(0x80ffe31e),
                        fillColor = Color(0x40ffff8b),
                        strokeWidth = 2f
                    )
                }
            }
        }

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