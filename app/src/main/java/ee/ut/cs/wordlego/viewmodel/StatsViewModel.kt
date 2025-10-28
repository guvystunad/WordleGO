package ee.ut.cs.wordlego.viewmodel

import androidx.lifecycle.ViewModel
import ee.ut.cs.wordlego.data.GameStats
import ee.ut.cs.wordlego.data.StatsRepository

class StatsViewModel(private val statsRepository: StatsRepository) : ViewModel() {

    val guessDistribution: Map<Int, Float>
        get() {
            val stats = statsRepository.getAllGameStats()
            val totalGames = stats.size.toFloat()
            if (totalGames == 0f) return emptyMap()

            return stats.filter { it.won }.groupBy { it.guesses }.mapValues { (_, games) ->
                games.size / totalGames
            }
        }

    fun addGameResult(guesses: Int, isWon: Boolean) {
        statsRepository.addGameStats(GameStats(guesses, isWon))
    }
}
