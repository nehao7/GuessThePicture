package com.example.guessthepicture.Fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.databinding.ActivityFwnlevel2Binding
import com.example.guessthepicture.databinding.CongratsDialogueBinding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding
import kotlin.random.Random

class FWNLevel2Fragment:Fragment() {
    lateinit var binding: ActivityFwnlevel2Binding
    private lateinit var pickerDialog: Dialog
    lateinit var mainActivity: MainActivity
    var firstrandomNumber=0
    var secondrandomNumber=0
    var thirdrandomNumber=0
    var randomNumbers = mutableListOf<Int>()
    private var mediaPlayer = MediaPlayer()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding= ActivityFwnlevel2Binding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.getSupportActionBar()?.hide()

        firstrandomNumber = generateNonRepeatingRandomNumber()
        var nextNumber = generateNonRepeatingRandomNumber()
        binding.imgmotheroriginal.setImageURI(Uri.parse(mainActivity.data[firstrandomNumber].picture))
//        binding.imgmother.setImageURI(Uri.parse(mainActivity.data[firstrandomNumber].picture))
//        Log.e("random", "firstrandom: nextNumber $nextNumber randomNumber $firstrandomNumber ", )
        binding.imgmother.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.reset()
                mediaPlayer.apply {
                    setDataSource(mainActivity.data[firstrandomNumber].audioRecord)
                    prepare()
                    start()
                }
                Log.d("Position State", "$firstrandomNumber")
//                adapter.updatePosition(position, 1)
            }else{
                mediaPlayer.pause()
//                adapter.updatePosition(position, 0)
            }
            Log.e("nextNumber", "$nextNumber randomNumber $firstrandomNumber")

        }

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
        binding.imgfather.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.reset()
                mediaPlayer.apply {
                    setDataSource(mainActivity.data[secondrandomNumber].audioRecord)
                    prepare()
                    start()
                }
                Log.d("Position State", "$secondrandomNumber")
//                adapter.updatePosition(position, 1)
            }else{
                mediaPlayer.pause()
//                adapter.updatePosition(position, 0)
            }
            Log.e("nextNumber", "$nextNumber randomNumber $secondrandomNumber")

        }
        binding.imgbrother.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.reset()
                mediaPlayer.apply {
                    setDataSource(mainActivity.data[thirdrandomNumber].audioRecord)
                    prepare()
                    start()
                }
                Log.d("Position State", "$thirdrandomNumber")
//                adapter.updatePosition(position, 1)
            }else{
                mediaPlayer.pause()
//                adapter.updatePosition(position, 0)
            }
            Log.e("nextNumber", "$nextNumber randomNumber $thirdrandomNumber")

        }

//        binding.imgfather.setImageURI(Uri.parse(mainActivity.data[secondrandomNumber].picture))
//        Log.e("random", "firstrandom: nextNumber $firstrandomNumber randomNumber $secondrandomNumber ", )

//        binding.imgbrother.setImageURI(Uri.parse(mainActivity.data[thirdrandomNumber].picture))
//        Log.e("random", "Secondrandom: nextNumber $secondrandomNumber randomNumber $thirdrandomNumber ", )


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

                        val builder = AlertDialog.Builder(mainActivity)
                        val rootView = CongratsDialogueBinding.inflate(layoutInflater)
                        builder.setView(rootView.root)
                        builder.setCancelable(true)
                        pickerDialog = builder.create()
                        val lp2 = WindowManager.LayoutParams()
                        val window: Window = pickerDialog.getWindow()!!
                        pickerDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                        lp2.copyFrom(window.attributes)
                        //This makes the dialog take up the full width
                        lp2.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        window.attributes = lp2
                        val dialogWindow: Window = pickerDialog.getWindow()!!
                        rootView.btnLevel2.visibility= View.GONE
                        mainActivity.navController.popBackStack()
//                        rootView.findViewById<Button>(R.id.btnLevel2).setOnClickListener {
//                            startActivity(Intent(this,Level2Activity::class.java))
//                        }
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
        lp2.width = ViewGroup.LayoutParams.WRAP_CONTENT
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = lp2
        val dialogWindow: Window = dialog.getWindow()!!
        dialogWindow.setGravity(Gravity.CENTER)
        dialog.show()
    }


}