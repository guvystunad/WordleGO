package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ee.ut.cs.wordlego.ui.components.Keyboard
import ee.ut.cs.wordlego.ui.components.LetterState
import ee.ut.cs.wordlego.ui.components.getLetterState
import ee.ut.cs.wordlego.GameState

@Composable
fun WordleGameScreen(
    navController: NavHostController,
    gameState: GameState,
    onGameStateChange: (GameState) -> Unit
) {
    var currentInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Wordle",
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Game Board
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    repeat(5) { col ->
                        val guess = gameState.guesses.getOrNull(row)
                        val letter = guess?.getOrNull(col)?.toString() ?: ""
                        val bgColor = if (guess != null) {
                            when (getLetterState(guess, col, gameState.targetWord)) {
                                LetterState.CORRECT -> Color(0xFF6AAA64)
                                LetterState.PRESENT -> Color(0xFFC9B458)
                                LetterState.ABSENT -> Color(0xFF787C7E)
                            }
                        } else if (row == gameState.guesses.size && col < currentInput.length) {
                            Color.White
                        } else {
                            Color.White
                        }

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
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = if (guess != null) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Keyboard
        Keyboard(
            onKeyPress = { key ->
                if (!gameState.isComplete) {
                    when (key) {
                        "ENTER" -> {
                            if (currentInput.length == 5) {
                                val newGuesses = gameState.guesses.toMutableList()
                                newGuesses.add(currentInput)
                                val isWon = currentInput == gameState.targetWord
                                val isComplete = isWon || newGuesses.size >= 6
                                onGameStateChange(
                                    gameState.copy(
                                        guesses = newGuesses,
                                        isComplete = isComplete,
                                        isWon = isWon
                                    )
                                )
                                currentInput = ""
                                if (isComplete) navController.navigate("stats")
                            }
                        }
                        "⌫" -> {
                            if (currentInput.isNotEmpty()) currentInput = currentInput.dropLast(1)
                        }
                        else -> {
                            if (currentInput.length < 5) currentInput += key
                        }
                    }
                }
            },
            gameState = gameState
        )
    }
}
