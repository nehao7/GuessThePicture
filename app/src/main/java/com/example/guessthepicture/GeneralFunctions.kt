package com.example.guessthepicture

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import com.example.guessthepicture.databinding.CongratsDialogueBinding
import java.io.ByteArrayOutputStream

interface ClickInterface{
    fun onButtonCLick()
}

enum class DialogType{
    happy, sad
}
class GeneralFunctions {

    companion object{
        fun convertBitmapToString(bitmap: Bitmap) : String{
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
            val byteArray = baos.toByteArray()
            val imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return imageString
        }

        fun showDialog(context : Context, layoutInflater: LayoutInflater, dialogType: DialogType, onClick: ClickInterface){
            var dialog = Dialog(context)
            var dialogBinding = CongratsDialogueBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)

            dialogBinding.btnLevel2.setOnClickListener {
                dialog.dismiss()
                onClick.onButtonCLick()
            }
            dialog.show()
        }
    }
}