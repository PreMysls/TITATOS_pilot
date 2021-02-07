package com.example.tictactoeonsteroids.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tictactoeonsteroids.data.GameArea
import com.example.tictactoeonsteroids.data.GameButton
import com.example.tictactoeonsteroids.database.Button
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import com.example.tictactoeonsteroids.engine.Engine
import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum
import com.example.tictactoeonsteroids.enums.PositionsEnum
import kotlinx.coroutines.launch

class GameViewModel(
    val database: GameDatabaseDao,
    application: Application, val gameId: Long
) : AndroidViewModel(application) {

    // ===== gameButton ======
        private val _gameButtonOne = MutableLiveData<GameButton>()
        val gameButtonOne: LiveData<GameButton>
        get() = _gameButtonOne

        private val _gameButtonTwo = MutableLiveData<GameButton>()
        val gameButtonTwo: LiveData<GameButton>
        get() = _gameButtonTwo

        private val _gameButtonThree = MutableLiveData<GameButton>()
        val gameButtonThree: LiveData<GameButton>
        get() = _gameButtonThree

        private val _gameButtonFour = MutableLiveData<GameButton>()
        val gameButtonFour: LiveData<GameButton>
        get() = _gameButtonFour

        private val _gameButtonFive = MutableLiveData<GameButton>()
        val gameButtonFive: LiveData<GameButton>
        get() = _gameButtonFive

        private val _gameButtonSix = MutableLiveData<GameButton>()
        val gameButtonSix: LiveData<GameButton>
        get() = _gameButtonSix

        private val _gameButtonSeven = MutableLiveData<GameButton>()
        val gameButtonSeven: LiveData<GameButton>
        get() = _gameButtonSeven

        private val _gameButtonEight = MutableLiveData<GameButton>()
        val gameButtonEight: LiveData<GameButton>
        get() = _gameButtonEight

        private val _gameButtonNine = MutableLiveData<GameButton>()
        val gameButtonNine: LiveData<GameButton>
        get() = _gameButtonNine

        // ====== CURRENT VALUES =====
        private val _colorActual = MutableLiveData<AttributeValueEnum>()
        val colorActual: LiveData<AttributeValueEnum>
        get() = _colorActual

        private val _backgroundActual = MutableLiveData<AttributeValueEnum>()
        val backgroundActual: LiveData<AttributeValueEnum>
        get() = _backgroundActual

        private val _scaleActual = MutableLiveData<AttributeValueEnum>()
        val scaleActual: LiveData<AttributeValueEnum>
        get() = _scaleActual

        //===== VISIBILITY =====
        private val _gameButtonsPlayable = MutableLiveData<Boolean>()
        val gameButtonsPlayable: LiveData<Boolean>
        get() = _gameButtonsPlayable

        private val _backgroundButtonPlayable = MutableLiveData<Boolean>()
        val backgroundButtonPlayable: LiveData<Boolean>
        get() = _backgroundButtonPlayable

        private val _scaleButtonPlayable = MutableLiveData<Boolean>()
        val scaleButtonPlayable: LiveData<Boolean>
        get() = _scaleButtonPlayable

        private val _colorButtonPlayable = MutableLiveData<Boolean>()
        val colorButtonPlayable: LiveData<Boolean>
        get() = _colorButtonPlayable

        private val _engineBackgroundButtonPlayable = MutableLiveData<Boolean>()
        val engineBackgroundButtonPlayable: LiveData<Boolean>
        get() = _engineBackgroundButtonPlayable

        private val _engineScaleButtonPlayable = MutableLiveData<Boolean>()
        val engineScaleButtonPlayable: LiveData<Boolean>
        get() = _engineScaleButtonPlayable

        private val _engineColorButtonPlayable = MutableLiveData<Boolean>()
        val engineColorButtonPlayable: LiveData<Boolean>
        get() = _engineColorButtonPlayable

    private var currentAttributeEnum: AttributeEnum = AttributeEnum.NONE

    private var currentBackgroundValueEnum: AttributeValueEnum = AttributeValueEnum.FIRST
    private var currentColorValueEnum: AttributeValueEnum = AttributeValueEnum.FIRST
    private var currentScaleValueEnum: AttributeValueEnum = AttributeValueEnum.FIRST

    //=====
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val gameButtonList = listOf(
        _gameButtonOne,
        _gameButtonTwo,
        _gameButtonThree,
        _gameButtonFour,
        _gameButtonFive,
        _gameButtonSix,
        _gameButtonSeven,
        _gameButtonEight,
        _gameButtonNine
    )
    private val gameArea = GameArea(gameButtonList)
    private val engine = Engine(gameArea)
    private val attributeButtonPlayableList =
        listOf(_backgroundButtonPlayable, _colorButtonPlayable, _scaleButtonPlayable)

    // ===== DATABASE AND COROUTINES =====
    private var buttons = MutableLiveData<List<Button>>()

    lateinit var winningAttribute : AttributeEnum
    lateinit var winningAttributeValue : AttributeValueEnum
    lateinit var winningPosition : PositionsEnum
    var playerWon: Boolean = false

    init {
        initializeGame(gameId)
    }

    // ===== GAME BUTTONS ======
    fun gameButtonPressed(position: Int) {
        if (isPositionEmpty(position)) {
            return
        } else {
            playerWon = true
            setAttributeValueToGameButton(position)

            setGameButtonsPlayable(false)
            setEngineAttributeButtonPlayableFalse()
            setAttributeButtonsPlayableTrue()
        }
    }

    // ====== ATTRIBUTES BUTTONS =====
    fun backgroundButtonPressed() {
        playerWon = false
        _backgroundActual.value = currentBackgroundValueEnum
        attributeButtonPressed(AttributeEnum.BACKGROUND)
    }

    fun colorButtonPressed() {
        playerWon = false
        _colorActual.value = currentColorValueEnum
        attributeButtonPressed(AttributeEnum.COLOR)
    }

    fun scaleButtonPressed() {
        playerWon = false
        _scaleActual.value = currentScaleValueEnum
        attributeButtonPressed(AttributeEnum.SCALE)
    }

    private fun attributeButtonPressed(attributeEnum: AttributeEnum) {
        currentAttributeEnum = attributeEnum
        enginePlaceAttributeValue(currentAttributeEnum)
        engineSelectAttribute()

        setGameButtonsPlayable(true)
        setAttributeButtonsPlayable(false)
        setEngineAttributeButtonPlayable(true)
    }

    // ===== VISIBILITY ======
    private fun playerIsOnMove(playerOnMove : Boolean){
        if(playerOnMove){
            setGameButtonsPlayable(false)
            setEngineAttributeButtonPlayableFalse()
            setAttributeButtonsPlayable(true)
        } else {
            engineSelectAttribute()

            setGameButtonsPlayable(true)
            setAttributeButtonsPlayable(false)
            setEngineAttributeButtonPlayableFalse()
            setEngineAttributeButtonPlayable(true)
        }
    }


    private fun setAttributeButtonsPlayableTrue() {
        _backgroundButtonPlayable.value = !engine.isAttributeFull(AttributeEnum.BACKGROUND)
        _colorButtonPlayable.value = !engine.isAttributeFull(AttributeEnum.COLOR)
        _scaleButtonPlayable.value = !engine.isAttributeFull(AttributeEnum.SCALE)
    }

    private fun setAttributeButtonsPlayable(playable: Boolean) {
        attributeButtonPlayableList.forEach { it.value = playable }
    }

    private fun setEngineAttributeButtonPlayable(playable: Boolean, attributeEnum: AttributeEnum = currentAttributeEnum) {
        when (attributeEnum) {
            AttributeEnum.BACKGROUND -> _engineBackgroundButtonPlayable.value = playable
            AttributeEnum.COLOR -> _engineColorButtonPlayable.value = playable
            AttributeEnum.SCALE -> _engineScaleButtonPlayable.value = playable
        }
    }

    private fun setEngineAttributeButtonPlayableFalse() {
        _engineBackgroundButtonPlayable.value = false
        _engineColorButtonPlayable.value = false
        _engineScaleButtonPlayable.value = false
    }

    private fun setGameButtonsPlayable(playable: Boolean) {
        _gameButtonsPlayable.value = playable
    }

    // ===== SETTING VALUES =====
    private fun setAttributeValueToGameButton(position: Int) {
        val gameButton = gameButtonList[position].value
        refreshGameButton(position, gameButton)
        gameButtonList[position].value = gameButton
        currentAttributeEnum = AttributeEnum.NONE
    }

    private fun refreshGameButton(position: Int, gameButton: GameButton?) {
        when (currentAttributeEnum) {
            AttributeEnum.BACKGROUND -> {
                updateBackground(position, currentBackgroundValueEnum.point)

                gameButton?.background = currentBackgroundValueEnum
                gameButtonList[position].value?.background = currentBackgroundValueEnum


                if(engine.isGameFinished(currentBackgroundValueEnum, currentAttributeEnum)){
                    setWinningParameters(currentAttributeEnum, currentBackgroundValueEnum)
                }

                currentBackgroundValueEnum = next(currentBackgroundValueEnum)
                _backgroundActual.value = currentBackgroundValueEnum
            }
            AttributeEnum.COLOR -> {
                updateColor(position, currentColorValueEnum.point)

                gameButton?.color = currentColorValueEnum
                gameButtonList[position].value?.color = currentColorValueEnum

                if(engine.isGameFinished(currentColorValueEnum, currentAttributeEnum)){
                    setWinningParameters(currentAttributeEnum, currentColorValueEnum)
                }

                currentColorValueEnum = next(currentColorValueEnum)
                _colorActual.value = currentColorValueEnum
            }
            AttributeEnum.SCALE -> {
                updateScale(position, currentScaleValueEnum.point)

                gameButton?.scale = currentScaleValueEnum
                gameButtonList[position].value?.scale = currentScaleValueEnum

                if(engine.isGameFinished(currentScaleValueEnum, currentAttributeEnum)){
                    setWinningParameters(currentAttributeEnum, currentScaleValueEnum)
                }

                currentScaleValueEnum = next(currentScaleValueEnum)
                _scaleActual.value = currentScaleValueEnum
            }
        }
        engine
    }

    // ===== ENGINE FUNCTIONS =====
    private fun engineSelectAttribute() {
        currentAttributeEnum = engine.chooseAttributeForPlayer()
        when (currentAttributeEnum) {
            AttributeEnum.BACKGROUND -> _backgroundActual.value = currentBackgroundValueEnum
            AttributeEnum.COLOR -> _colorActual.value = currentColorValueEnum
            AttributeEnum.SCALE -> _scaleActual.value = currentScaleValueEnum
        }
    }

    private fun enginePlaceAttributeValue(attributeEnum: AttributeEnum) {
        val position = engine.play(attributeEnum)
        setAttributeValueToGameButton(position)
    }

    private fun isPositionEmpty(position: Int) =
        (gameButtonList[position].value?.getAttribute(currentAttributeEnum)?.point != AttributeValueEnum.DEFAULT.point)

    fun next(attributeValue: AttributeValueEnum) = AttributeValueEnum.getOpposite(attributeValue)

    // ===== DATABASE =====
    private suspend fun getAllButtons(gameId: Long) = database.getButtons(gameId)
    private suspend fun updateBackgroundInDb(rank: Int, background: Int) = database.updateBackground(gameId, rank, background)
    private suspend fun updateColorInDb(rank: Int, color: Int) = database.updateColor(gameId, rank, color)
    private suspend fun updateScaleInDb(rank: Int, scale: Int) = database.updateScale(gameId, rank, scale)
    private suspend fun getCurrentGame(gameId: Long) = database.getGameById(gameId)

    // ===== COROUTINES =====
    private fun initializeGame(gameId: Long){
        viewModelScope.launch {
            buttons.value = getAllButtons(gameId)
            for (rank in 0..8) {
                val background = AttributeValueEnum.getValueByPoint(buttons.value!![rank].background)
                val color = AttributeValueEnum.getValueByPoint(buttons.value!![rank].color)
                val scale = AttributeValueEnum.getValueByPoint(buttons.value!![rank].scale)
                gameButtonList[rank].value = GameButton(background, color, scale)
            }
            initializeAttributeValues()
            initializeButtonsPlayability(getCurrentGame(gameId).playerStarted)
        }
    }

    private fun updateBackground(rank: Int, background: Int){
        viewModelScope.launch {
            updateBackgroundInDb(rank, background)
        }
    }
    private fun updateColor(rank: Int, color: Int){
        viewModelScope.launch {
            updateColorInDb(rank, color)
        }
    }
    private fun updateScale(rank: Int, scale: Int){
        viewModelScope.launch {
            updateScaleInDb(rank, scale)
        }
    }
    // ===== INITIALIZING HELPER FUNCTIONS =====
    private fun initializeButtonsPlayability(playerStartedGame: Boolean){
            val countOfDefaultValues = engine.getCountOfValues(AttributeEnum.BACKGROUND, AttributeValueEnum.DEFAULT) +
            engine.getCountOfValues(AttributeEnum.COLOR, AttributeValueEnum.DEFAULT) +
            engine.getCountOfValues(AttributeEnum.SCALE, AttributeValueEnum.DEFAULT)
            playerIsOnMove(playerStartedGame != (countOfDefaultValues % 2 == 0))
    }

    private fun initializeAttributeValues(){
        currentBackgroundValueEnum = engine.getCurrentValueOfAttribute(AttributeEnum.BACKGROUND)
        currentColorValueEnum = engine.getCurrentValueOfAttribute(AttributeEnum.COLOR)
        currentScaleValueEnum = engine.getCurrentValueOfAttribute(AttributeEnum.SCALE)

        _backgroundActual.value = currentBackgroundValueEnum
        _colorActual.value = currentColorValueEnum
        _scaleActual.value = currentScaleValueEnum
    }
    // ====== GAME FINISHED =====
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun setWinningParameters(currentAttribute: AttributeEnum, currentValue: AttributeValueEnum){
        winningAttribute = currentAttribute
        winningAttributeValue = currentValue
        winningPosition = engine.getPositionsByScore(winningAttributeValue.threePoints, winningAttribute)!!
        _eventGameFinish.value = true
    }
}
