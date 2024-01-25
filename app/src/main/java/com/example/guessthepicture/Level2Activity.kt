package com.example.guessthepicture

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.guessthepicture.databinding.ActivityLevel2Binding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding
import kotlin.random.Random

class Level2Activity : Fragment(){

    lateinit var binding:ActivityLevel2Binding
    private lateinit var pickerDialog: Dialog
    lateinit var mainActivity: MainActivity
    var firstrandomNumber=0
    var secondrandomNumber=0
    var thirdrandomNumber=0

    var randomNumbers = mutableListOf<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding= ActivityLevel2Binding.inflate(layoutInflater)
        return binding.root  }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mainActivity.navController.popBackStack(R.id.action_Level2Activity_to_gameLevelsFragment2,true)
        firstrandomNumber = generateNonRepeatingRandomNumber()
        var nextNumber = generateNonRepeatingRandomNumber()
        binding.imgmotheroriginal.setImageURI(Uri.parse(mainActivity.data[firstrandomNumber].picture))
        binding.imgmother.setImageURI(Uri.parse(mainActivity.data[firstrandomNumber].picture))
        Log.e("random", "firstrandom: nextNumber $nextNumber randomNumber $firstrandomNumber ", )


        secondrandomNumber = generateNonRepeatingRandomNumber()
        while(firstrandomNumber == nextNumber ){
            nextNumber = generateNonRepeatingRandomNumber()
        }
        while( secondrandomNumber == firstrandomNumber ){
            secondrandomNumber = generateNonRepeatingRandomNumber()
        }
//        thirdrandomNumber=generateRandomNumber()
         thirdrandomNumber = generateNonRepeatingRandomNumber(firstrandomNumber,secondrandomNumber)
        while (thirdrandomNumber==secondrandomNumber){
            thirdrandomNumber=generateNonRepeatingRandomNumber()
        }

        binding.imgfather.setImageURI(Uri.parse(mainActivity.data[secondrandomNumber].picture))
        Log.e("random", "firstrandom: nextNumber $firstrandomNumber randomNumber $secondrandomNumber ", )

        binding.imgbrother.setImageURI(Uri.parse(mainActivity.data[thirdrandomNumber].picture))
        Log.e("random", "Secondrandom: nextNumber $secondrandomNumber randomNumber $thirdrandomNumber ", )





//        getSupportActionBar()?.hide()

        binding.imgmotheroriginal.setOnLongClickListener { v ->
            val dragShadowBuilder = View.DragShadowBuilder(v)
            v.startDragAndDrop(null, dragShadowBuilder, v, 0)
            true
        }

        binding.imgbrother.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v ==  binding.imgbrother) {
                        showtryAgain()
//                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
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

        binding.imgmother.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v == binding.imgmother) {

                        GeneralFunctions.showDialog(mainActivity, layoutInflater,DialogType.happy, object : ClickInterface{
                            override fun onButtonCLick() {
                                mainActivity.navController.popBackStack()
                            }
                        })
                    }
                    else{
                        showtryAgain()

//                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    private fun generateNonRepeatingRandomNumber(excludeNumber1: Int? = -1,excludeNumber2: Int? = -1): Int {
        val totalImages = mainActivity.data.size
        if (randomNumbers.size == totalImages) {
            // If all numbers are used, clear the list to start again
            randomNumbers.clear()
        }

        var randomNumber: Int
        do {
            // Generate a random number until a non-repeating one is found
            randomNumber = Random.nextInt(totalImages)
        } while (randomNumbers.contains(randomNumber) || randomNumber == excludeNumber1)

        // Add the number to the list to ensure it's not repeated
        randomNumbers.add(randomNumber)

        return randomNumber
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