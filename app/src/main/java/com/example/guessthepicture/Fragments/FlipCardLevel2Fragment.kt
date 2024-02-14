package com.example.guessthepicture.Fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.R
import com.example.guessthepicture.databinding.ActivityFlipCardLevel2Binding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding
import kotlin.random.Random

class FlipCardLevel2Fragment : Fragment() {
    lateinit var binding: ActivityFlipCardLevel2Binding
    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    private lateinit var pickerDialog: Dialog

    lateinit var front_anim_father: AnimatorSet
    lateinit var back_anim_father: AnimatorSet

    lateinit var front_anim_brother : AnimatorSet
    lateinit var back_anim_brother :AnimatorSet
    var isFrontMother =true
    var isFrontFather=true
    var isFrontBrother=true
    lateinit var mainActivity: MainActivity
    var randomNumber=0
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
        binding= ActivityFlipCardLevel2Binding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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



        randomNumber = Random.nextInt(mainActivity.data.size)
        binding.imgmotheroriginal.setImageURI(Uri.parse(mainActivity.data[randomNumber].picture))
        binding.imgmother.setImageURI(Uri.parse(mainActivity.data[randomNumber].picture))

////        var nextNumber = Random.nextInt(mainActivity.data.size)
//        var secondRandom = Random.nextInt(mainActivity.data.size)
//        while(randomNumber == nextNumber ){
//            nextNumber = Random.nextInt(mainActivity.data.size)
//        }
//        while(secondRandom == nextNumber && secondRandom == randomNumber ){
//            secondRandom = Random.nextInt(mainActivity.data.size)
//        }
//
//        binding.imgfather.setImageURI(Uri.parse(mainActivity.data[nextNumber].picture))
//        print("nextNumber $nextNumber randomNumber $randomNumber")
//
//        binding.imgbrother.setImageURI(Uri.parse(mainActivity.data[secondRandom].picture))
//        print("nextNumber $nextNumber randomNumber $randomNumber")


        var scale = mainActivity.resources.displayMetrics.density

        val frontmother = binding.imgmother as ImageView
        val backmother = binding.imgmotherback as ImageView

        val frontfather = binding.imgfather as ImageView
        val backfather = binding.imgfatherback as ImageView

        val frontbrother=binding.imgbrother as ImageView
        val backbrother=binding.imgbrotherback as ImageView
        val flip = binding.btnFlip as Button

        frontmother.cameraDistance = 8000 * scale
        backmother.cameraDistance = 8000 * scale

        frontfather.cameraDistance = 8000 * scale
        backfather.cameraDistance = 8000 * scale

        frontbrother.cameraDistance = 8000 * scale
        backbrother.cameraDistance = 8000 * scale

        front_anim = AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.front_flip_anim
        ) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.back_flip_animator
        ) as AnimatorSet

        front_anim_father = AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.front_flip_anim
        ) as AnimatorSet
        back_anim_father= AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.back_flip_animator
        ) as AnimatorSet

        front_anim_brother = AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.front_flip_anim
        ) as AnimatorSet
        back_anim_brother= AnimatorInflater.loadAnimator(
            mainActivity,
            R.animator.back_flip_animator
        ) as AnimatorSet

        binding.imgmother.setOnClickListener {
            if (isFrontMother) {
                front_anim.setTarget(frontmother);
                back_anim.setTarget(backmother);
                front_anim.start()
                back_anim.start()
                isFrontMother = false

            } else {
                back_anim.setTarget(backmother)
                front_anim.setTarget(frontmother)
                front_anim.start()
                back_anim.start()
                isFrontMother = true

            }


        }

        binding.imgfather.setOnClickListener {
            if (isFrontFather) {
                front_anim_father.setTarget(frontfather);
                back_anim_father.setTarget(backfather);
                front_anim_father.start()
                back_anim_father.start()
                isFrontFather = false

            } else {
                back_anim_father.setTarget(backfather)
                front_anim_father.setTarget(frontfather)
                front_anim_father.start()
                back_anim_father.start()
                isFrontFather = true

            }


        }

        binding.imgbrother.setOnClickListener {
            if (isFrontBrother) {
                front_anim_brother.setTarget(frontbrother);
                back_anim_brother.setTarget(backbrother);
                front_anim_brother.start()
                back_anim_brother.start()
                isFrontBrother = false

            } else {
                back_anim_brother.setTarget(backbrother)
                front_anim_brother.setTarget(frontbrother)
                front_anim_brother.start()
                back_anim_brother.start()
                isFrontBrother = true

            }


        }


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

                        rootView.findViewById<Button>(R.id.btnLevel2).visibility=View.GONE

                        val lp = dialogWindow.attributes
                        dialogWindow.setGravity(Gravity.CENTER)
                        pickerDialog.show()
//                        Toast.makeText(mainActivity, "Congratulations", Toast.LENGTH_SHORT).show()
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
//                        Toast.makeText(mainActivity, "Try Again", Toast.LENGTH_SHORT).show()
//                        binding.imgmother.text = "Dropped!"
                    }

                }
            }
            true
        }

        binding.imgbrother.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop
                    val droppedView = event.localState as View
                    if (v ==  binding.imgbrother) {
                        // Perform actions when the view is dropped on the target
                        showtryAgain()
                    }

                }
            }
            true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding= ActivityFlipCardLevel2Binding.inflate(layoutInflater)

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