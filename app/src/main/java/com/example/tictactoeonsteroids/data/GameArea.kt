package com.example.tictactoeonsteroids.data

import androidx.lifecycle.MutableLiveData

data class GameArea(val gameButtonList: List<MutableLiveData<GameButton>>)