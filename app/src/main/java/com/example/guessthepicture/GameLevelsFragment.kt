package com.example.guessthepicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            mainActivity.navController.navigate(R.id.matchImagesL1Fragment)
           // startActivity(Intent(this,MatchImagesL1Fragment::class.java))

        }
        binding.Game2.setOnClickListener {
//            startActivity(Intent(this,FlipCardLevel1Activity::class.java))
        }
        binding.Game3.setOnClickListener {
//            startActivity(Intent(this,AudioRecordActivity::class.java))
        }
    }

}