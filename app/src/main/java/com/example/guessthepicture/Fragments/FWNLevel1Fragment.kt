package com.example.guessthepicture.Fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guessthepicture.ClickInterface
import com.example.guessthepicture.DialogType
import com.example.guessthepicture.GeneralFunctions
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.R
import com.example.guessthepicture.databinding.ActivityFwnlevel1Binding
import com.example.guessthepicture.databinding.TryAgainDialogueBinding

class FWNLevel1Fragment : Fragment(){
    lateinit var binding:ActivityFwnlevel1Binding
    private lateinit var pickerDialog: Dialog
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding = ActivityFwnlevel1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        getSupportActionBar()?.hide()

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
                        GeneralFunctions.showDialog(
                            mainActivity,
                            layoutInflater,
                            DialogType.happy,
                            fragment = FWNLevel1Fragment(),
                            object : ClickInterface {
                                override fun onButtonCLick() {
                                    mainActivity.navController.navigate(R.id.action_FWNLevel1Activity_to_FWNLevel2Activity)
                                }
                            })

                    }
                    else{
                        Toast.makeText(mainActivity, "Try Again", Toast.LENGTH_SHORT).show()
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
//                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
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


}