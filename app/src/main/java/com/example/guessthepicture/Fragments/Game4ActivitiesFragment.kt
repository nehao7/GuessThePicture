package com.example.guessthepicture.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.R
import com.example.guessthepicture.adapters.Game4ViewpagerAdapter
import com.example.guessthepicture.databinding.FragmentGame4ActivitiesBinding

class Game4ActivitiesFragment : Fragment() {
    lateinit var binding:FragmentGame4ActivitiesBinding
    lateinit var mainActivity: MainActivity

    //    private lateinit var viewPager: ViewPager
//    private var isScrollEnabled = false
//    var imgposition=-1
//    private lateinit var game4ViewpagerAdapter: Game4ViewpagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGame4ActivitiesBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMallVisit.setOnClickListener {
            mainActivity.navController.navigate(R.id.action_game4ActivitiesFragment_to_visitTheMallFragment)
        }
//        viewPager = binding.viewPager
//
//        val imageList = listOf(
//            R.drawable.enter_the_door,
//            R.drawable.pickup_trolley,
//            R.drawable.go_to_shop,
//            // Add more image resource IDs as needed
//        )
//
//        game4ViewpagerAdapter = Game4ViewpagerAdapter(requireContext(), imageList)
//        viewPager.adapter = game4ViewpagerAdapter
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                imgposition=position
//            }
//
//            override fun onPageSelected(position: Int) {
////                showImageChangeDialog(position)
//                imgposition=position
//
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//        viewPager.setOnTouchListener { _, _ -> true }
//        binding.btnnextstep.setOnClickListener {
//            showImageChangeDialog(imgposition)
//        }
//        binding.btnPrevious.setOnClickListener {
//            showPreviousImageDialog(imgposition)
//        }

    }
//    private fun enableDisableSwipe(enable: Boolean) {
//        isScrollEnabled = enable
//        viewPager.setOnTouchListener { _, event ->
//            !isScrollEnabled || event.action == MotionEvent.ACTION_MOVE
//        }
//    }
//
//    private fun showImageChangeDialog(position: Int) {
////        enableDisableSwipe(false)
//        val alertDialogBuilder = AlertDialog.Builder(context)
//        alertDialogBuilder.setTitle("Image Change")
//        alertDialogBuilder.setMessage("Do you want to move to the next image?")
//        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
//            dialog.dismiss()
//            // Move to the next image
//            viewPager.setCurrentItem(position + 1, true)
////            enableDisableSwipe(true)
//        }
//        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss()
//            viewPager.setCurrentItem(imgposition,true)
////            enableDisableSwipe(true)
//
//
//
//            // Do nothing or handle accordingly
//        }
//        alertDialogBuilder.create().show()
//    }
//
//    private fun showPreviousImageDialog(position: Int) {
////        enableDisableSwipe(false)
//        val alertDialogBuilder = AlertDialog.Builder(context)
//        alertDialogBuilder.setTitle("Image Change")
//        alertDialogBuilder.setMessage("Do you want to move to the previous image?")
//        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
//            dialog.dismiss()
//            // Move to the next image
//            viewPager.setCurrentItem(position - 1, true)
////            enableDisableSwipe(true)
//        }
//        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss()
//            viewPager.setCurrentItem(imgposition,true)
////            enableDisableSwipe(true)
//
//
//
//            // Do nothing or handle accordingly
//        }
//        alertDialogBuilder.create().show()
//    }
}



