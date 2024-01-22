package com.example.guessthepicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.guessthepicture.databinding.ActivityGameLevelsBinding

class GameLevelsActivity : AppCompatActivity() {
    lateinit var binding:ActivityGameLevelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()

        binding.Game1.setOnClickListener {
            startActivity(Intent(this,Level1Activity::class.java))
            onBackPressed()
        }
        binding.Game2.setOnClickListener {
            startActivity(Intent(this,FlipCardLevel1Activity::class.java))
            onBackPressed()
        }
        binding.Game3.setOnClickListener {
            startActivity(Intent(this,AudioRecordActivity::class.java))
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}