package com.example.guessthepicture.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.R
import com.example.guessthepicture.databinding.ActivityGameLevelsBinding

class GameLevelsFragment : Fragment() {
    lateinit var binding:ActivityGameLevelsBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityGameLevelsBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Game1.setOnClickListener {
            mainActivity.navController.navigate(R.id.action_gameLevelsFragment_to_matchImagesL1Fragment)
        }
        binding.Game2.setOnClickListener {
            mainActivity.navController.navigate(R.id.flipCardLevel1Activity)
        }
        binding.Game3.setOnClickListener {
            mainActivity.navController.navigate(R.id.FWNLevel1Activity)
        }
        binding.Game4.setOnClickListener {
            mainActivity.navController.navigate(R.id.action_gameLevelsFragment_to_game4ActivitiesFragment)
        }
    }

}