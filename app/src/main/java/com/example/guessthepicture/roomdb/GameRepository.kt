package com.example.guessthepicture.roomdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameRepository(private val gameRepository: GameInterface) {

    val readAllData: LiveData<List<PersonEntity>> = gameRepository.getAllPersons()

}