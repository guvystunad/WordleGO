package ee.ut.cs.wordlego

data class GameState(
    val guesses: MutableList<String> = mutableListOf(),
    val currentGuess: String = "",
    val targetWord: String?,
    val isComplete: Boolean = false,
    val isWon: Boolean = false
)
