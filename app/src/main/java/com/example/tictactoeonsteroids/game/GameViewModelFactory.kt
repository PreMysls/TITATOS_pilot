package com.example.tictactoeonsteroids.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoeonsteroids.database.GameDatabaseDao

class GameViewModelFactory(
    private val dataSource: GameDatabaseDao,
    private val application: Application,
    private val gameId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(
                dataSource, application, gameId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}