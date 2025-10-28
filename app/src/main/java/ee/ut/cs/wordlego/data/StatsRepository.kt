package ee.ut.cs.wordlego.data

class StatsRepository {
    private val gameStats = mutableListOf<GameStats>()

    fun addGameStats(stats: GameStats) {
        gameStats.add(stats)
    }

    fun getAllGameStats(): List<GameStats> {
        return gameStats.toList()
    }
}
