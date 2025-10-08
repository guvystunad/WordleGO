package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(navController: NavHostController, onLocationSelected: (LatLng) -> Unit) {
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
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
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

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }
}
