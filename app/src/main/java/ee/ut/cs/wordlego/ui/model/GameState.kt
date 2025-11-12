package ee.ut.cs.wordlego

data class GameState(
    val guesses: MutableList<String> = mutableListOf(),
    val currentGuess: String = "",
    val targetWord: String?= null,
    val isComplete: Boolean = false,
    val isWon: Boolean = false

)
