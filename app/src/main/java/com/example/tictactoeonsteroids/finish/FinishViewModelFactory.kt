package com.example.tictactoeonsteroids.finish

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.enums.PositionsEnum


class FinishViewModelFactory (
private val dataSource: GameDatabaseDao,
private val application: Application,
private val playerWon: Boolean,
private val positions: PositionsEnum,
private val attribute: AttributeEnum,
private val attributeValue: AttributeValueEnum
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishViewModel::class.java)) {
            return FinishViewModel(
                dataSource, application, playerWon, positions, attribute, attributeValue
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}