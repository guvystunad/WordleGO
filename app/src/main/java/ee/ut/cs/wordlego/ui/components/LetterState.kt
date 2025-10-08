package ee.ut.cs.wordlego.ui.components

import androidx.compose.ui.graphics.Color
enum class LetterState { CORRECT, PRESENT, ABSENT }

fun getLetterState(guess: String, position: Int, target: String): LetterState {
    val letter = guess[position]
    return when {
        target[position] == letter -> LetterState.CORRECT
        target.contains(letter) -> LetterState.PRESENT
        else -> LetterState.ABSENT
    }
}

fun getKeyColor(key: String, gameState: ee.ut.cs.wordlego.GameState): Color {
    if (key == "ENTER" || key == "⌫") return Color(0xFFD3D6DA)

    var bestState: LetterState? = null
    gameState.guesses.forEach { guess ->
        guess.forEachIndexed { index, char ->
            if (char.toString() == key) {
                val state = getLetterState(guess, index, gameState.targetWord)
                if (bestState == null ||
                    (state == LetterState.CORRECT) ||
                    (state == LetterState.PRESENT && bestState != LetterState.CORRECT)
                ) bestState = state
            }
        }
    }

    return when (bestState) {
        LetterState.CORRECT -> Color(0xFF6AAA64)
        LetterState.PRESENT -> Color(0xFFC9B458)
        LetterState.ABSENT -> Color(0xFF787C7E)
        null -> Color(0xFFD3D6DA)
    }
}
