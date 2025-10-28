package ee.ut.cs.wordlego

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import ee.ut.cs.wordlego.data.StatsRepository
import ee.ut.cs.wordlego.ui.screens.HomeScreen
import ee.ut.cs.wordlego.ui.screens.MapScreen
import ee.ut.cs.wordlego.ui.screens.StatsScreen
import ee.ut.cs.wordlego.ui.screens.WordleGameScreen
import ee.ut.cs.wordlego.viewmodel.StatsViewModel
import ee.ut.cs.wordlego.viewmodel.StatsViewModelFactory

@Composable
fun WordleGOApp(userLocation: LatLng?, context: Context) {
    val navController = rememberNavController()
    var gameState by remember {
        mutableStateOf<GameState?>(null)
    }

    val statsRepository = remember { StatsRepository() }
    val statsViewModel: StatsViewModel = viewModel(factory = StatsViewModelFactory(statsRepository))

    LaunchedEffect(Unit) {
        val word = WordRepository.loadRandomWord(context)
        gameState = GameState(targetWord = word)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation ?: LatLng(59.437, 24.7536),
            12f
        )

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
                onGameStateChange = { updatedGameState ->
                    gameState = updatedGameState
                    if (updatedGameState.isComplete) {
                        statsViewModel.addGameResult(updatedGameState.guesses.size, updatedGameState.isWon)
                    }
                }
            )
        }
        composable("stats") {
            StatsScreen(navController, statsViewModel)
        }
    }
}
