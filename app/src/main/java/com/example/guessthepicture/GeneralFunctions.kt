package com.example.guessthepicture

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class GeneralFunctions {

    companion object{
        fun convertBitmapToString(bitmap: Bitmap) : String{
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
            val byteArray = baos.toByteArray()
            val imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return imageString
        }
    }
}