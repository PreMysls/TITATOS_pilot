package com.example.tictactoeonsteroids.finish

import android.app.Application
import androidx.lifecycle.*
import com.example.tictactoeonsteroids.database.Game
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.enums.PositionsEnum
import kotlinx.coroutines.launch

class FinishViewModel(
    val database: GameDatabaseDao,
    application: Application,
    playerWon: Boolean,
    positions: PositionsEnum,
    attribute: AttributeEnum,
    attributeValue: AttributeValueEnum
) : AndroidViewModel(application) {

    private val _navigateToGame = MutableLiveData<Long>()
    val navigateToGame: LiveData<Long>
        get() = _navigateToGame

    private var currentGame = MutableLiveData<Game?>()
    var gameId = MutableLiveData<Long>()

    fun onNewGame() {
        initializeNewGame()
    }

    fun doneNavigating() {
        _navigateToGame.value = null
    }

    // ===== DATABASE AND COROUTINES =====
    private suspend fun getGameFromDatabase() = database.getCurrentGame()
    private suspend fun insertNewGame(game: Game) { database.insertGame(game) }
    private suspend fun insertButtons(gameId: Long) { database.insertButtonsToGame(gameId) }

    private fun initializeNewGame() {
        viewModelScope.launch {
            insertNewGame(Game())
            currentGame.value = getGameFromDatabase()
            gameId.value = currentGame.value!!.gameId
            insertButtons(gameId.value!!)
            _navigateToGame.value = gameId.value
        }
    }
}