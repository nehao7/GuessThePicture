package com.example.guessthepicture.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.GameRepository
import com.example.guessthepicture.roomdb.PersonEntity
import kotlinx.coroutines.Dispatchers

class GamesViewModel(application: Application): AndroidViewModel(application) {
    val personList: LiveData<List<PersonEntity>>

    private val repository:GameRepository
    init {
        val taskDao=GameDB.getDatabase(application).gameInterface()
        repository= GameRepository(taskDao)
        personList=repository.readAllData
    }

   /* fun insertPerson(task: PersonEntity){
        viewModelScope.launch (Dispatchers.IO) {
            repository(task)

        }
    }
    fun updateTask(task: PersonEntity){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updatePerson(task)

        }
    }
    fun deletePerson(task: PersonEntity){
        viewModelScope.launch (Dispatchers.IO) {
            repository.removePerson(task)

        }
    }*/

    fun getPerson(): LiveData<List<PersonEntity>> {
        return personList
    }
}