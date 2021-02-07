package com.example.tictactoeonsteroids.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    var gameId: Long = 0L,

    @ColumnInfo(name = "game_ended")
    var gameEnded: Boolean = false,

    @ColumnInfo(name = "player_started")
    var playerStarted: Boolean = true
    ) {
    override fun toString(): String {
        return "\n=== GAME ID: $gameId ENDED: $gameEnded PLAYER STARTED: $playerStarted ===\n"
//        return super.toString()
    }
}
