package com.example.guessthepicture

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.guessthepicture.Fragments.FWNLevel1Fragment
import com.example.guessthepicture.Fragments.FWNLevel2Fragment
import com.example.guessthepicture.Fragments.FlipCardLevel1Fragment
import com.example.guessthepicture.Fragments.FlipCardLevel2Fragment
import com.example.guessthepicture.Fragments.Level2Fragnent
import com.example.guessthepicture.Fragments.MatchImagesL1Fragment
import com.example.guessthepicture.databinding.CongratsDialogueBinding
import java.io.ByteArrayOutputStream

interface ClickInterface{
    fun onButtonCLick()
}


enum class DialogType{
    happy, sad,

}

enum class DialogClickType{
    Image, Delete, Audio,Play
}

class GeneralFunctions{


    companion object{
        fun convertBitmapToString(bitmap: Bitmap) : String{
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
            val byteArray = baos.toByteArray()
            val imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return imageString
        }

        fun showDialog(context : Context,
                       layoutInflater: LayoutInflater,
                       dialogType: DialogType,
                      fragment: Fragment,
//                       button: Button? = null,
                       onClick: ClickInterface){
            var dialog = Dialog(context)
            var dialogBinding = CongratsDialogueBinding.inflate(layoutInflater)
//            val dialogButton: Button = button ?: dialog.findViewById(R.id.btnLevel2)
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)

            if (fragment is MatchImagesL1Fragment || fragment is FlipCardLevel1Fragment ||fragment is FWNLevel1Fragment){
                dialogBinding.btnLevel2.text="Level 2"
            }
            else if (fragment is Level2Fragnent || fragment is FlipCardLevel2Fragment ||fragment is FWNLevel2Fragment){
                dialogBinding.btnLevel2.text="Exit"
            }else{
                dialogBinding.btnLevel2.text="Level 2"
            }


            dialogBinding.btnLevel2.setOnClickListener {
                dialog.dismiss()
                onClick.onButtonCLick()
            }
         if (dialogType==DialogType.happy){

//            if (fragment==level2Activity){
//                dialogBinding.btnLevel2.setText("Exit")
//            }

         }else if (dialogType==DialogType.sad){
             dialogBinding.btnLevel2.visibility=View.GONE
             dialogBinding.imgHappyEmoji.setImageResource(R.drawable.sad_smiley)
             dialogBinding.txttitlehappy.setText("Try Again")
             dialog.setCancelable(true)
         }

            dialog.show()
        }
    }
}