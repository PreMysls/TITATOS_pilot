package com.example.tictactoeonsteroids.firstscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tictactoeonsteroids.database.Game
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import kotlinx.coroutines.launch

class FirstScreenViewModel(val database: GameDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private val _navigateToGame = MutableLiveData<Long>()
    val navigateToGame: LiveData<Long>
        get() = _navigateToGame

    private var currentGame = MutableLiveData<Game?>()
    var gameId = MutableLiveData<Long>()

    fun onNewGame() {
        initializeNewGame()
    }

    fun onResumeGame() {
        initializeOldGame()
    }

    fun doneNavigating() {
        _navigateToGame.value = null
    }

    // ===== DATABASE AND COROUTINES =====
    private suspend fun getGameFromDatabase() = database.getCurrentGame()
    private suspend fun insertNewGame(game: Game) { database.insertGame(game) }
    private suspend fun insertButtons(gameId: Long) { database.insertButtonsToGame(gameId) }

    private fun initializeOldGame() {
        viewModelScope.launch {
            currentGame.value = getGameFromDatabase()
            gameId.value = currentGame.value!!.gameId
            _navigateToGame.value = gameId.value
        }
    }

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