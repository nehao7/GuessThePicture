package com.example.guessthepicture.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.GameRepository
import com.example.guessthepicture.roomdb.PersonEntity

class GamesViewModel(application: Application): AndroidViewModel(application) {
    val taskList: LiveData<List<PersonEntity>>

    private val repository:GameRepository
    init {
        val taskDao=GameDB.getDatabase(application).gameInterface().getAllPersons()
        repository= GameRepository(taskDao)
        taskList=repository.readAllData
    }
    fun insertTask(task: Task){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertTask(task)

        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateTask(task)

        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch (Dispatchers.IO) {
            repository.removeTask(task)

        }
    }

    fun getTask(): LiveData<List<Task>> {
        return taskList
    }
}