package com.example.tictactoeonsteroids

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tictactoeonsteroids.database.Button
import com.example.tictactoeonsteroids.database.Game
import com.example.tictactoeonsteroids.database.GameDatabase
import com.example.tictactoeonsteroids.database.GameDatabaseDao
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class GameDatabaseTest {

    private lateinit var gameDao: GameDatabaseDao
    private lateinit var db: GameDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, GameDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        gameDao = db.gameDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertGame() {
        val game = Game()
        gameDao.insertGame(game)
        gameDao.insertGame(game)
        val gameCount = gameDao.getGameCount()
        assertEquals(2, gameCount)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertButton() {
        val button = Button()
        gameDao.insertButton(button)
        val buttonCount = gameDao.getButtonCount()
        assertEquals(1, buttonCount)
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertGameAndButtons() {
//        gameDao.insertGameAndButtons()
//        val gameCount = gameDao.getGameCount()
//        val buttonCount = gameDao.getButtonCount()
//
//        assertEquals(1, gameCount)
//        assertEquals(8, buttonCount)
//    }
}

