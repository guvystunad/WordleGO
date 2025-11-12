package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ee.ut.cs.wordlego.ui.components.Keyboard
import ee.ut.cs.wordlego.ui.components.LetterState
import ee.ut.cs.wordlego.ui.components.getLetterState
import ee.ut.cs.wordlego.GameState
import ee.ut.cs.wordlego.ui.components.BackButton
import androidx.compose.ui.text.font.FontWeight
import ee.ut.cs.wordlego.WordRepository.fetchRandomWord

@Composable
fun WordleGameScreen(
    navController: NavHostController,
    gameState: GameState,
    onGameStateChange: (GameState) -> Unit,
    locationName: String
) {
    var currentInput by remember { mutableStateOf("") }
    var isLoading by remember {mutableStateOf(true)}
    val context = LocalContext.current
    var gameState by remember { mutableStateOf(gameState) }
    LaunchedEffect(Unit) {
        val newWord = fetchRandomWord(context, locationName) ?: "SQUAD"
        gameState = (gameState.copy(targetWord = newWord))
        isLoading = false
    }

//Main container for the screen
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        //show loading while fetching the word
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            BackButton(
                navController = navController,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Wordle",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
//Game board
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(6) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            repeat(5) { col ->
                                val guess = gameState.guesses.getOrNull(row)
                                val letter = guess?.getOrNull(col)?.toString() ?: ""
                                //determine background color based on letter state
                                val bgColor = if (guess != null) {
                                    when (getLetterState(guess, col, gameState.targetWord ?: "SQUAD")) {
                                        LetterState.CORRECT -> Color(0xFF6AAA64)
                                        LetterState.PRESENT -> Color(0xFFC9B458)
                                        LetterState.ABSENT -> Color(0xFF787C7E)
                                    }
                                } else if (row == gameState.guesses.size && col < currentInput.length) {
                                    Color.White
                                } else {
                                    Color.White
                                }
// individual letter box
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(bgColor, RoundedCornerShape(4.dp))
                                        .border(2.dp, Color(0xFFD3D6DA), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (row == gameState.guesses.size && col < currentInput.length) {
                                            currentInput[col].toString()
                                        } else {
                                            letter
                                        },
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (guess != null) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
// keyboard composable
                Keyboard(
                    onKeyPress = { key ->
                        if (!gameState.isComplete) {
                            when (key) {
                                "ENTER" -> {
                                    //submit the guess if 5 letters are typed
                                    if (currentInput.length == 5) {
                                        val newGuesses = gameState.guesses.toMutableList()
                                        newGuesses.add(currentInput)

                                        val isWon = currentInput.equals(
                                            gameState.targetWord,
                                            ignoreCase = true
                                        )
                                        val isComplete = isWon || newGuesses.size >= 6
                                        //update game state

                                        gameState = gameState.copy(
                                            guesses = newGuesses,
                                            isComplete = isComplete,
                                            isWon = isWon
                                        )
                                        //reset current input
                                        currentInput = ""
                                        if (isComplete) navController.navigate("stats")
                                    }
                                }

                                "⌫" -> {
                                    //delete last letter
                                    if (currentInput.isNotEmpty()) currentInput =
                                        currentInput.dropLast(1)
                                }


                            else -> {
                                // add letter if space is available
                                if (currentInput.length < 5) currentInput += key
                            }
                        }
                        }
                    },
                    gameState= gameState

                )
            }
        }
    }
}

