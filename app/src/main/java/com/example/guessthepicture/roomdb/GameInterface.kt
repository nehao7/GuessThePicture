package com.example.guessthepicture.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface GameInterface {
    @Insert
    suspend fun insertPerson(personEntity: PersonEntity)

    @Query("Select * from PersonEntity")
    fun getAllPersons() : LiveData<List<PersonEntity>>

    @Update
    suspend fun updatePerson(personEntity:PersonEntity)

    @Delete
    suspend fun deletePerson(personEntity: PersonEntity)
}