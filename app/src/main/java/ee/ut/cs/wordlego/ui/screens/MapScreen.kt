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

    val wordleLocations = listOf(
        LatLng(58.3780, 26.7290),
        LatLng(58.3850, 26.7200),
        LatLng(58.3720, 26.7400)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties()
        ) {
            wordleLocations.forEach { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "Wordle Location",
                    onClick = {
                        onLocationSelected(location)
                        true
                    }
                )
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
