package com.example.guessthepicture

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.guessthepicture.databinding.ActivityFwnlevel1Binding
import com.example.guessthepicture.databinding.CongratsDialogueBinding
import com.example.guessthepicture.databinding.PhotoDisplayViewItemBinding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding

class FWNLevel1Activity : AppCompatActivity() {
    lateinit var binding:ActivityFwnlevel1Binding
    private lateinit var pickerDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFwnlevel1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()

        binding.imgmotheroriginal.setOnLongClickListener { v ->
            val dragShadowBuilder = View.DragShadowBuilder(v)
            v.startDragAndDrop(null, dragShadowBuilder, v, 0)
            true
        }

        binding.tvmother.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v == binding.tvmother) {
                        // Perform actions when the view is dropped on the target

                        val builder = AlertDialog.Builder(this)
                        val rootView=CongratsDialogueBinding.inflate(layoutInflater)
                        builder.setView(rootView.root)
                        builder.setCancelable(true)
                        pickerDialog = builder.create()
                        val lp2 = WindowManager.LayoutParams()
                        val window: Window = pickerDialog.getWindow()!!
                        pickerDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                        lp2.copyFrom(window.attributes)
                        //This makes the dialog take up the full width
                        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
                        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        window.attributes = lp2
                        val dialogWindow: Window = pickerDialog.getWindow()!!
                        rootView.btnLevel2.setOnClickListener {
                            startActivity(Intent(this,FWNLevel2Activity::class.java))
                            onBackPressed()
                        }
                        val lp = dialogWindow.attributes
                        dialogWindow.setGravity(Gravity.CENTER)
                        pickerDialog.show()
//                        Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
//                        binding.imgmother.text = "Dropped!"
                    }
                    else{
                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        binding.tvfather.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v ==  binding.tvfather) {
                        // Perform actions when the view is dropped on the target
                        var dialog = Dialog(this)
                        val rootView =  TryAgainDialogueBinding.inflate(layoutInflater)
                        dialog.setContentView(rootView.root)
                        dialog.setCancelable(true)
                        val lp2 = WindowManager.LayoutParams()
                        val window: Window = dialog.getWindow()!!
                        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        lp2.copyFrom(window.attributes)
                        //This makes the dialog take up the full width
                        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
                        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        window.attributes = lp2
                        val dialogWindow: Window = dialog.getWindow()!!
                        dialogWindow.setGravity(Gravity.CENTER)
                        dialog.show()
//                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
//                        binding.imgmother.text = "Dropped!"
                    }

                }
            }
            true
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}