package com.example.guessthepicture.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface GameInterface {
    @Insert
    suspend fun insertPerson(personEntity: PersonEntity)

    @Query("Select * from PersonEntity")
    suspend fun getAllPersons() : List<PersonEntity>

    @Update
        suspend fun updatePerson(personEntity:PersonEntity)
}