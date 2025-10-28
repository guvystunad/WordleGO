package ee.ut.cs.wordlego.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ee.ut.cs.wordlego.data.StatsRepository

class StatsViewModelFactory(private val statsRepository: StatsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(statsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
