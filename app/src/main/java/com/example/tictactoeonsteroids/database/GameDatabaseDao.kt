package com.example.tictactoeonsteroids.database

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.tictactoeonsteroids.data.GameButton

@Dao
interface GameDatabaseDao {

    @Insert
    suspend fun insertGame(game: Game)

    @Insert
    suspend fun insertButton(button: Button)

    @Update
    fun updateGame(game: Game)

    @Query("UPDATE button_table SET background = :background WHERE gameButtonId = :gameId AND rank = :rank")
    suspend fun updateBackground(gameId: Long, rank: Int, background : Int)

    @Query("UPDATE button_table SET color = :color WHERE gameButtonId = :gameId AND rank = :rank")
    suspend fun updateColor(gameId: Long, rank: Int, color : Int)

    @Query("UPDATE button_table SET scale = :scale WHERE gameButtonId = :gameId AND rank = :rank")
    suspend fun updateScale(gameId: Long, rank: Int, scale : Int)

    @Query("UPDATE button_table SET background = :background, color = :color, scale = :scale WHERE gameButtonId = :gameId AND rank = :rank")
    suspend fun updateButtons(gameId: Long, rank: Int, background : Int, color: Int, scale :Int)

    @Query("SELECT gameId FROM game_table ORDER BY gameId DESC LIMIT 1")
    suspend fun getLastGameId() : Long

    @Query("SELECT * FROM game_table ORDER BY gameId DESC LIMIT 1")
    suspend fun getCurrentGame(): Game

    @Query("SELECT * FROM game_table WHERE gameId = :gameId")
    suspend fun getGameById(gameId: Long): Game

    @Transaction
    suspend fun updateButtonList(gameId: Long, buttonList:  List<MutableLiveData<GameButton>>){
        for(rank in 0 .. 8) {updateButtons(gameId, rank, buttonList[rank].value!!.background.point, buttonList[rank].value!!.color.point, buttonList[rank].value!!.scale.point)}
    }

    @Transaction
    suspend fun updateButtonList2(gameId: Long, buttonList:  List<GameButton>){
        for(rank in 0 .. 8) {updateButtons(gameId, rank, buttonList[rank].background.point, buttonList[rank].color.point, buttonList[rank].scale.point)}
    }

    @Transaction
    suspend fun insertButtonsToGame(gameId: Long) {
        for (i in 0 .. 8) {insertButton(Button(gameButtonId = gameId, rank = i))}
    }

    @Transaction
    @Query("SELECT * FROM game_table ORDER BY gameId DESC LIMIT 10")
    suspend fun getGameAndButtons(): List<GameAndButtons>

    @Query("SELECT * FROM button_table WHERE gameButtonId = :gameId ORDER BY rank ASC")
    suspend fun getButtons(gameId: Long): List<Button>

    @Query("SELECT COUNT(gameId) FROM game_table")
    fun getGameCount() : Int

    @Query("SELECT COUNT(buttonId) FROM button_table")
    fun getButtonCount() : Int
}