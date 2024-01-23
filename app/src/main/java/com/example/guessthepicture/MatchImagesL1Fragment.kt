package com.example.guessthepicture

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.guessthepicture.databinding.FragmentMatchImagesL1Binding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding
import kotlin.random.Random

class MatchImagesL1Fragment : Fragment() {
    lateinit var binding: FragmentMatchImagesL1Binding
    private lateinit var pickerDialog: Dialog
    lateinit var mainActivity: MainActivity
    var randomNumber = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding= FragmentMatchImagesL1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomNumber = Random.nextInt(mainActivity.data.size)
        binding.imgmotheroriginal.setImageURI(Uri.parse(mainActivity.data[randomNumber].picture))
        binding.imgmother.setImageURI(Uri.parse(mainActivity.data[randomNumber].picture))
        var nextNumber = Random.nextInt(mainActivity.data.size)
        while(randomNumber == nextNumber ){
            nextNumber = Random.nextInt(mainActivity.data.size)
        }

        binding.imgfather.setImageURI(Uri.parse(mainActivity.data[nextNumber].picture))
        print("nextNumber $nextNumber randomNumber $randomNumber")
        binding.imgmotheroriginal.setOnLongClickListener { v ->
            val dragShadowBuilder = View.DragShadowBuilder(v)
            v.startDragAndDrop(null, dragShadowBuilder, v, 0)
            true
        }

        binding.imgmother.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v == binding.imgmother) {
                        // Perform actions when the view is dropped on the target

                        val builder = AlertDialog.Builder(mainActivity)
                        val inflater = layoutInflater
                        val rootView: View = inflater.inflate(R.layout.congrats_dialogue, null)
                        builder.setView(rootView)
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

                        rootView.findViewById<Button>(R.id.btnLevel2).setOnClickListener {

                        }
                        val lp = dialogWindow.attributes
                        dialogWindow.setGravity(Gravity.CENTER)
                        pickerDialog.show()
//                        Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
//                        binding.imgmother.text = "Dropped!"
                    }
                    else{
                        Toast.makeText(mainActivity, "Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        binding.imgfather.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v ==  binding.imgfather) {
                        // Perform actions when the view is dropped on the target
                        showtryAgain()
//                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
//                        binding.imgmother.text = "Dropped!"
                    }

                }
            }
            true
        }

        binding.btnNext.setOnClickListener{
          //  startActivity(Intent(this,Level2Activity::class.java))
        }
    }

    private fun showtryAgain(){
        var dialog = Dialog(mainActivity)
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
    }

}