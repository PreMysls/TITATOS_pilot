package com.example.tictactoeonsteroids.database

import androidx.room.Embedded

import androidx.room.Relation

data class GameAndButtons(
    @Embedded val game: Game,
    @Relation(parentColumn = "gameId", entityColumn = "gameButtonId") val buttons: List<Button>
)
