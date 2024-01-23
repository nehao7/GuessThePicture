package com.example.guessthepicture

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.guessthepicture.databinding.ActivityFlipCardLevel1Binding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding

class FlipCardLevel1Activity : Fragment() {
    lateinit var binding : ActivityFlipCardLevel1Binding
    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    private lateinit var pickerDialog: Dialog

    lateinit var front_anim_father: AnimatorSet
    lateinit var back_anim_father: AnimatorSet
    var isFrontMother =true
    var isFrontFather =true
    var isFrontBrother=true
    lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding = ActivityFlipCardLevel1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var scale = mainActivity.resources.displayMetrics.density

        val frontmother = binding.imgmother as ImageView
        val backmother = binding.imgmotherback as ImageView

        val frontfather = binding.imgfather as ImageView
        val backfather = binding.imgfatherback as ImageView

        val flip = binding.btnFlip as Button

        frontmother.cameraDistance = 8000 * scale
        backmother.cameraDistance = 8000 * scale

        frontfather.cameraDistance = 8000 * scale
        backfather.cameraDistance = 8000 * scale

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
                            pickerDialog.dismiss()
                            mainActivity.navController.navigate(R.id.flipCardLevel2Activity)

                        }
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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