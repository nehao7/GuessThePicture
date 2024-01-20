package com.example.guessthepicture.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface GameInterface {
    @Insert
    suspend fun insertPerson(personEntity: PersonEntity)

    @Query("Select * from PersonEntity")
    suspend fun getAllPersons() : List<PersonEntity>

}