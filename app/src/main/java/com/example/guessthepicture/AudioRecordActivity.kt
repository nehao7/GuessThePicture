package com.example.guessthepicture

import android.Manifest.permission.RECORD_AUDIO
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessthepicture.adapters.AddAudioAdapter
import com.example.guessthepicture.adapters.AddPhotoAdapter
import com.example.guessthepicture.databinding.ActivityAudioRecordBinding
import com.example.guessthepicture.databinding.AudioRecordLayoutBinding
import com.example.guessthepicture.databinding.AudioRecordOptionBinding
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.PersonEntity
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class AudioRecordActivity : AppCompatActivity() {

    lateinit var binding: ActivityAudioRecordBinding
    lateinit var gameDB: GameDB
    lateinit var linearLayoutManager: LinearLayoutManager
    var list = ArrayList<PersonEntity>()
    private lateinit var recorder: MediaRecorder
    private var mediaPlayer =  MediaPlayer()
    lateinit var adapter: AddAudioAdapter
    var audiofile=""
    var audioPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                ShowDialog()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
              /*  var alertDialog = AlertDialog.Builder(this)
                alertDialog.apply {
                    setTitle("Permission required")
                    setMessage("Permission required to run the app")
                    setCancelable(false)
                    setPositiveButton("Ok"){_,_-> openSettings()}
                }
                alertDialog.show()*/
            }
        }

/*    private fun openSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    getSupportActionBar()?.hide()

    adapter = AddAudioAdapter(list,
            object : AddAudioAdapter.itemClickListener{
                override fun onItemClick(position: Int, audio: String) {
                    if(!mediaPlayer.isPlaying){
                        mediaPlayer.reset()
                        mediaPlayer.apply {
                            setDataSource(audio)
                            prepare()
                            start()
                        }
                        Log.d("Position State", "$position 1")
//            adapter.updatePosition(position, 1)
                    }else{
                        mediaPlayer.pause()
//            adapter.updatePosition(position, 0)
                    }
                }
            })
        gameDB = GameDB.getDatabase(this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcList.layoutManager = linearLayoutManager
        binding.rcList.adapter = adapter

        binding.fabAdd.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                audioPermission.launch(RECORD_AUDIO)
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                ShowDialog()

            }
        }
        binding.btnplayGame.setOnClickListener {
            startActivity(Intent(this,FWNLevel1Activity::class.java))
        }

        getAudioFiles()
        mediaPlayer.setOnCompletionListener {
//            adapter.updatePosition(-1, 0)
        }
    }

    fun ShowDialog(){
        var dialogBinding = AudioRecordOptionBinding.inflate(layoutInflater)
        var dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        recorder = MediaRecorder()
        var dirPath = "${externalCacheDir?.absolutePath}/"

        val simpleDateFormat = SimpleDateFormat("DD-MM-yyyy_mm.ss.hh")
        val date = simpleDateFormat.format(Date())
        var filename = "audio_record_$date"

         audiofile = "$dirPath$filename.mp3"
        dialogBinding.startRecord.setOnClickListener {
            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile("$dirPath$filename.mp3")

                try {
                    prepare()
                    start()
                } catch (e: IOException) {

                }
            }

        }

        dialogBinding.stoprecord.setOnClickListener {
            recorder.stop()
        }

        dialogBinding.btnPlayRecordRecord.setOnClickListener {
            mediaPlayer.setDataSource("$dirPath$filename.mp3")
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
        dialogBinding.btnSave.setOnClickListener {
            recorder.stop()
            try {
                var fos = FileOutputStream("$dirPath$filename")
                var out = ObjectOutputStream(fos)
                out.close()
                fos.close()
            } catch (e: IOException) {

            }

            var record = PersonEntity()
            for (i in list.indices){
                record.audioRecord=list[i].audioRecord
            }
            lifecycleScope.launch {
                list.addAll( gameDB.gameInterface().getAllPersons())
            }
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getAudioFiles(){
        list.clear()
       lifecycleScope.launch {
           list.addAll(gameDB.gameInterface().getAllPersons())
       }
    }


}