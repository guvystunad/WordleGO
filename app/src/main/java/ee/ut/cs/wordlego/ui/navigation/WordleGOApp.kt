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



@Composable
fun WordleGOApp(userLocation: LatLng?, context: Context) {
    val navController = rememberNavController()
    var gameState by remember {
        mutableStateOf<GameState?> (null)
    }

    LaunchedEffect(Unit) {
        val word = WordRepository.loadRandomWord(context)
        gameState = GameState(targetWord = word)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation ?: LatLng(59.437, 24.7536),
            12f)

    }

    if (gameState == null) {
        return
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("map") {
            MapScreen(
                navController = navController,
                onLocationSelected = { location ->
                    gameState = GameState(targetWord = WordRepository.loadRandomWord(context))
                    navController.navigate("wordle")
                },
                userLocation = userLocation
            )
        }
        composable("wordle") {
            WordleGameScreen(
                navController,
                gameState = gameState!!,
                onGameStateChange = { gameState = it }
            )
        }
        composable("stats") {
            StatsScreen(navController)
        }
    }
}
