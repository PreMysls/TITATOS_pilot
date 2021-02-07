package com.example.tictactoeonsteroids.engine

import com.example.tictactoeonsteroids.data.GameArea
import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.enums.PositionsEnum


class Engine(private val gameArea: GameArea) {

    // ===== SETTING VALUE ======
    fun play(attributeEnum: AttributeEnum): Int {
        val currentValue = getCurrentValueOfAttribute(attributeEnum)
        return when {
            couldWin(currentValue, attributeEnum) != -1 -> couldWin(currentValue, attributeEnum)
            couldLose(currentValue, attributeEnum) != -1 -> couldLose(currentValue, attributeEnum)
            playCornerOrCenter(attributeEnum) != -1 -> playCornerOrCenter(attributeEnum)
            else -> playRandomEmptyButton(attributeEnum)
        }
    }

    private fun couldWin(currentValueEnum: AttributeValueEnum, attributeEnum: AttributeEnum) =
        getEmptyWinningButton(currentValueEnum.twoPoints, attributeEnum)

    private fun couldLose(currentValueEnum: AttributeValueEnum, attributeEnum: AttributeEnum) =
        couldWin(AttributeValueEnum.getOpposite(currentValueEnum), attributeEnum)

    private fun playCornerOrCenter(attributeEnum: AttributeEnum) =
        getPositionOfEmptyButton(
            PositionsEnum.CORNER_AND_CENTER.coordinates.shuffled().toSet(),
            attributeEnum
        )

    private fun playRandomEmptyButton(attributeEnum: AttributeEnum) =
        gameArea.gameButtonList.indexOf(
            gameArea.gameButtonList.shuffled().first { it.value?.getAttribute(attributeEnum)!!.point == 0 })

    // ===== CHOOSING ATTRIBUTE FOR PLAYER ======
    fun chooseAttributeForPlayer(): AttributeEnum {
        val attributes =
            mutableListOf(AttributeEnum.BACKGROUND, AttributeEnum.COLOR, AttributeEnum.SCALE)
        removeFullAttributes(attributes)
        val nonFullAttributes = mutableListOf<AttributeEnum>().apply { addAll(attributes)}
        removeWinningAttributeForOpponent(attributes)
        return attributes.shuffled().firstOrNull() ?: nonFullAttributes.random()
    }

    private fun removeFullAttributes(attributeEnums: MutableList<AttributeEnum>) {
        attributeEnums.removeIfFull(AttributeEnum.BACKGROUND)
        attributeEnums.removeIfFull(AttributeEnum.COLOR)
        attributeEnums.removeIfFull(AttributeEnum.SCALE)
    }

    private fun removeWinningAttributeForOpponent(attributeEnums: MutableList<AttributeEnum>) {
        attributeEnums.removeIfCouldWin(AttributeEnum.BACKGROUND)
        attributeEnums.removeIfCouldWin(AttributeEnum.COLOR)
        attributeEnums.removeIfCouldWin(AttributeEnum.SCALE)
    }

    // ===== GETTING POSITION =====
    private fun getPositionOfEmptyButton(positions: Set<Int>, attributeEnum: AttributeEnum) =
        gameArea.gameButtonList.indexOf(gameArea.gameButtonList.filterIndexed { index, gameButton -> index in positions }
            .firstOrNull { it.value?.getAttribute(attributeEnum)?.point == 0 })

    private fun getEmptyWinningButton(winScore: Int, attributeEnum: AttributeEnum) =
        when (winScore) {
            getScoreByPosition(
                PositionsEnum.FIRST_ROW.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.FIRST_ROW.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.SECOND_ROW.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.SECOND_ROW.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.THIRD_ROW.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.THIRD_ROW.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.FIRST_COLUMN.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.FIRST_COLUMN.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.SECOND_COLUMN.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.SECOND_COLUMN.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.THIRD_COLUMN.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.THIRD_COLUMN.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.FIRST_DIAGONAL.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.FIRST_DIAGONAL.coordinates, attributeEnum)
            getScoreByPosition(
                PositionsEnum.SECOND_DIAGONAL.coordinates,
                attributeEnum
            ) -> getPositionOfEmptyButton(PositionsEnum.SECOND_DIAGONAL.coordinates, attributeEnum)
            else -> -1
        }

    // ===== SCORE COUNTING =====
    fun getPositionsByScore(score: Int, attributeEnum: AttributeEnum) = PositionsEnum.values()
        .firstOrNull { score == getScoreByPosition(it.coordinates, attributeEnum) }

    private fun getScoreByPosition(positions: Set<Int>, attributeEnum: AttributeEnum) =
        gameArea.gameButtonList.filterIndexed { index, _ -> index in positions }
            .sumBy { it.value?.getAttribute(attributeEnum)!!.point }

    // ===== REMOVING =====
    private fun MutableList<AttributeEnum>.removeIfFull(attributeEnum: AttributeEnum) {
        if ((gameArea.gameButtonList.count { it.value?.getAttribute(attributeEnum)?.point == AttributeValueEnum.DEFAULT.point }) == 0) this.remove(
            attributeEnum
        )
    }

    private fun MutableList<AttributeEnum>.removeIfCouldWin(attributeEnum: AttributeEnum) {
        val currentValue = getCurrentValueOfAttribute(attributeEnum)
        if (couldWin(currentValue, attributeEnum) != -1) this.remove(attributeEnum)
    }

    fun getCurrentValueOfAttribute(attributeEnum: AttributeEnum): AttributeValueEnum {
        val countOfFirstValues = getCountOfValues(attributeEnum, AttributeValueEnum.FIRST)
        val countOfSecondValues = getCountOfValues(attributeEnum, AttributeValueEnum.SECOND)
        return if (countOfFirstValues <= countOfSecondValues) AttributeValueEnum.FIRST else AttributeValueEnum.SECOND
    }

    fun getCountOfValues(attributeEnum: AttributeEnum, attributeValueEnum: AttributeValueEnum) =
        gameArea.gameButtonList.count { it.value?.getAttribute(attributeEnum)?.point == attributeValueEnum.point }

    fun isAttributeFull(attributeEnum: AttributeEnum) = AttributeValueEnum.DEFAULT.point * 9 == getCountOfValues(attributeEnum, AttributeValueEnum.DEFAULT)

    // ===== GAME FINISHED =====
    fun isGameFinished(currentValueEnum: AttributeValueEnum, attributeEnum: AttributeEnum) =
        getPositionsByScore(currentValueEnum.threePoints, attributeEnum) != null
}