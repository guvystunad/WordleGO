package ee.ut.cs.wordlego

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ee.ut.cs.wordlego.ui.screens.HomeScreen
import ee.ut.cs.wordlego.ui.screens.MapScreen
import ee.ut.cs.wordlego.ui.screens.WordleGameScreen
import ee.ut.cs.wordlego.ui.screens.StatsScreen

@Composable
fun WordleGOApp() {
    val navController = rememberNavController()
    var gameState by remember { mutableStateOf(GameState()) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("map") {
            MapScreen(navController) { location ->
                gameState = GameState()
                navController.navigate("wordle")
            }
        }
        composable("wordle") {
            WordleGameScreen(
                navController,
                gameState = gameState,
                onGameStateChange = { gameState = it }
            )
        }
        composable("stats") {
            StatsScreen(navController)
        }
    }
}
