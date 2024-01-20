package com.example.guessthepicture.roomdb

import android.media.AudioRecord
import android.speech.tts.Voice
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PersonEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var picture : String?= "",
    var name : String ?= "",
    var relation:String?="",
    var audioRecord: String=""
)