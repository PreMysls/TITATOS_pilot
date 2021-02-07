package com.example.tictactoeonsteroids.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "button_table")
data class Button(
    @PrimaryKey(autoGenerate = true)
    var buttonId: Long = 0L,

    @ColumnInfo(name = "gameButtonId")
    var gameButtonId: Long = -1L,

    @ColumnInfo(name = "rank")
    var rank: Int = -1,

    @ColumnInfo(name = "background")
    var background: Int = 0,

    @ColumnInfo(name = "color")
    var color: Int = 0,

    @ColumnInfo(name = "scale")
    var scale: Int = 0
) {
    override fun toString(): String {
        return "  $rank    background: $background color: $color scale: $scale \n"
//        return super.toString()
    }
}
