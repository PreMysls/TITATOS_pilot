package com.example.tictactoeonsteroids.firstscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoeonsteroids.database.GameDatabaseDao

class FirstScreenViewModelFactory(
    private val dataSource: GameDatabaseDao,
    private val application: Application

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstScreenViewModel::class.java)) {
            return FirstScreenViewModel(
                dataSource, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}