package com.example.guessthepicture.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.guessthepicture.R

@Database(version = 1, entities = [PersonEntity::class])
abstract class GameDB : RoomDatabase() {

    abstract  fun gameInterface() : GameInterface
    companion object{
        var gameDatabase : GameDB ?= null
        fun getDatabase(context: Context) : GameDB{
            if(gameDatabase == null){
                gameDatabase = Room.databaseBuilder(context,
                    GameDB::class.java,
                    context.getString(R.string.app_name))
                    .build()
            }
            return gameDatabase!!
        }
    }
}