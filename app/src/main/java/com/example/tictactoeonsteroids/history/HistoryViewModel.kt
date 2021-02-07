package com.example.tictactoeonsteroids.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.tictactoeonsteroids.database.GameAndButtons
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import kotlinx.coroutines.launch


class HistoryViewModel(val database: GameDatabaseDao, application: Application) : AndroidViewModel(application) {

    private val gamesAndButtons = MutableLiveData<List<GameAndButtons>>()
    var gamesAndButtonsString =
        Transformations.map(gamesAndButtons) { transformGamesAndButtonsToString(allGames = gamesAndButtons.value!!) }

    init {
        initializeGamesAndButtons()
    }

    private fun initializeGamesAndButtons() {
        viewModelScope.launch {
            gamesAndButtons.value = getGamesAndButtonsFromDatabase()
        }
    }

    private suspend fun getGamesAndButtonsFromDatabase() = database.getGameAndButtons()

    private fun transformGamesAndButtonsToString(allGames: List<GameAndButtons>): String {
        var result = ""
        allGames.forEach { result += it.game.toString()
            it.buttons.forEach { button -> result += button.toString() }
        }
        return result
    }
}
