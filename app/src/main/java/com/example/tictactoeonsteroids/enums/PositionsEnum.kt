package com.example.tictactoeonsteroids.enums

enum class PositionsEnum(val coordinates: Set<Int>) {
    FIRST_ROW(setOf(0, 1, 2)),
    SECOND_ROW(setOf(3, 4, 5)),
    THIRD_ROW(setOf(6, 7, 8)),

    FIRST_COLUMN(setOf(0, 3, 6)),
    SECOND_COLUMN(setOf(1, 4, 7)),
    THIRD_COLUMN(setOf(2, 5, 8)),

    FIRST_DIAGONAL(setOf(0, 4, 8)),
    SECOND_DIAGONAL(setOf(2, 4, 6)),

    CORNER_AND_CENTER(setOf(0, 2, 4, 6, 8)),
    ALL(setOf(0, 1, 2, 3, 4, 5, 6, 7, 8))
}