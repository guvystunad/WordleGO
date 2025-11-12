package ee.ut.cs.wordlego

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.rememberCameraPositionState
import ee.ut.cs.wordlego.ui.screens.HomeScreen
import ee.ut.cs.wordlego.ui.screens.MapScreen
import ee.ut.cs.wordlego.ui.screens.WordleGameScreen
import ee.ut.cs.wordlego.ui.screens.StatsScreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import android.content.Context
import ee.ut.cs.wordlego.WordRepository.fetchRandomWord
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment



@Composable
fun WordleGOApp(userLocation: LatLng?, context: Context) {
    val navController = rememberNavController()
    var gameState by remember {
        mutableStateOf<GameState?> (null)
    }
    val scope = rememberCoroutineScope()

//camera state for google maps
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation ?: LatLng(59.437, 24.7536),
            12f)

    }

//Navigation host managing all screens
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("map") {
            MapScreen(
                navController = navController,
                onLocationSelected = { locationName ->
                    navController.navigate("wordle/$locationName")
                },
                userLocation = userLocation
            )
        }
       //wordle game screen
        composable("wordle/{locationName}") { backStackEntry ->
            //Retrieve location name
            val locationName = backStackEntry.arguments?.getString("locationName") ?: "delta"
            //Local game state for this screen
            var localGameState by remember { mutableStateOf<GameState?>(null) }

            LaunchedEffect(locationName) {
                val word = fetchRandomWord(context, locationName) ?: "SQUAD"
                localGameState = GameState(targetWord = word)
            }

            // Show loading while word is fetched
            if (localGameState == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                //show wordle game screen when word is ready
                WordleGameScreen(
                    navController = navController,
                    gameState = localGameState!!,
                    onGameStateChange = { gameState = it },
                    locationName = locationName
                )
            }
        }

        composable("stats") {
            StatsScreen(navController)
        }
    }
}
