package com.example.guessthepicture.Fragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.canhub.cropper.CropImageContract
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.PersonEntity
import kotlinx.coroutines.launch
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.guessthepicture.adapters.AddPhotoAdapter
import com.example.guessthepicture.databinding.PhotoDisplayViewItemBinding
import com.example.guessthepicture.viewModels.GamesViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guessthepicture.DialogClickType
import com.example.guessthepicture.MainActivity
import com.example.guessthepicture.R
import com.example.guessthepicture.databinding.AudioRecordOptionBinding
import com.example.guessthepicture.databinding.FragmentIntroductionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class IntroductionFragment : Fragment(), View.OnClickListener{
    lateinit var binding: FragmentIntroductionBinding
    lateinit var addPhotoAdapter: AddPhotoAdapter
    private  var recorder: MediaRecorder ?= null
    private var mediaPlayer = MediaPlayer()
    private var audiofile=""
    var dirPath=""
    var filename=""
    var record = PersonEntity()
    var personToUpdate=PersonEntity()
    private var dialog:Dialog?=null
    private var captureImgUri:Uri?=null
    private var bitmap : Bitmap ? = null
    lateinit var gameDB : GameDB
    private var galleryUri:Uri?=null
    private var photoDisplayViewItemBinding: PhotoDisplayViewItemBinding ?= null
    private val TAG = IntroductionFragment::class.java.canonicalName
    lateinit var mainActivity: MainActivity
    private var gamesViewModel : GamesViewModel ?= null
    private var allowCamera = false
    private var allowGallery = false
    private var mediaPermission = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S)
        Manifest.permission.READ_EXTERNAL_STORAGE
    else{
        Manifest.permission.READ_MEDIA_IMAGES
    }
    private var audioPermission = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S)
        Manifest.permission.READ_EXTERNAL_STORAGE
    else{
        Manifest.permission.READ_MEDIA_AUDIO
    }
    private var cameraPermission = Manifest.permission.CAMERA

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                mediaPermission
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                mainActivity,
                cameraPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getImagePermission.launch(arrayOf(mediaPermission, cameraPermission))
        } else{
            allowGallery = true
            allowCamera = true
            launchCropImage()
        }
    }

    private var getImagePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        for ((key, value) in it){
            if(key == mediaPermission){
                allowGallery = true
            }else if(key == cameraPermission){
                allowCamera = true
            }
        }
        launchCropImage()
    }

    private var audioPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
           Toast.makeText(mainActivity, mainActivity.resources.getString(R.string.record_audio), Toast.LENGTH_SHORT).show()
        }
    }

    fun launchCropImage(){
        cropImage.launch(CropImageContractOptions(uri = null,
            cropImageOptions = CropImageOptions(
                imageSourceIncludeCamera = allowCamera,
                imageSourceIncludeGallery = allowGallery,
            ),))
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(mainActivity) // optional usage
            captureImgUri=result!!.originalUri
            galleryUri = result.uriContent
            bitmap = result.bitmap
            Log.e(TAG, " bitmap $bitmap ${result.bitmap} $result captureImgUri $captureImgUri $galleryUri $uriContent")
            photoDisplayViewItemBinding?.img?.setImageURI(galleryUri)
        } else {
            // An error occurred.
            val exception = result.error
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        gameDB = GameDB.getDatabase(mainActivity)
        initviews()
        dialog?.dismiss()

        mediaPlayer.setOnCompletionListener {
           mainActivity.data.add(PersonEntity(audioRecord = record.audioRecord))

        }
        binding.recycleraddphoto.layoutManager = GridLayoutManager(mainActivity,3)
        addPhotoAdapter = AddPhotoAdapter(mainActivity,mainActivity.data,
            object :AddPhotoAdapter.ViewHandler{
                override fun viewHandler(position: Int, clickType : DialogClickType) {
                    when(clickType){
                        DialogClickType.Play ->{

                            if(!mediaPlayer.isPlaying){
                                mediaPlayer.reset()
                                mediaPlayer.apply {
                                    setDataSource(mainActivity.data[position].audioRecord)
                                    prepare()
                                    start()
                                }
                                Log.d("Position State", "$position 1")
                            }else{
                                mediaPlayer.pause()
                            }
                        }
                        DialogClickType.Audio ->{
                            if( ContextCompat.checkSelfPermission(
                                    mainActivity,
                                    Manifest.permission.RECORD_AUDIO
                                ) != PackageManager.PERMISSION_GRANTED){
                                audioPermissionRequest.launch(Manifest.permission.RECORD_AUDIO)
                            }else if(ContextCompat.checkSelfPermission(
                                    mainActivity,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED){
                                personToUpdate=mainActivity.data[position]
                                ShowAudioDialog(position)
                            }
//                            if(ContextCompat.checkSelfPermission(mainActivity, audioPermission) == PackageManager.PERMISSION_GRANTED){
//                                ShowAudioDialog(position)
//                            }
//                        else{
//                                audioPermissionRequest.launch(audioPermission)
//                            }

                        }
                        DialogClickType.Delete ->{
                            AlertDialog.Builder(mainActivity).apply {
                                setTitle(mainActivity.resources.getString(R.string.alert))
                                setMessage(mainActivity.resources.getString(R.string.delete_person))
                                setPositiveButton(mainActivity.resources.getString(R.string.yes)){ _, _->
                                    lifecycleScope.launch {
                                        gameDB.gameInterface().deletePerson(mainActivity.data[position])
                                    }
                                }
                                setNegativeButton(mainActivity.resources.getString(R.string.no)){ _, _->}
                                show()
                            }
                        }

                        else -> {}
                    }
                }
            })
        addPhotoAdapter.notifyDataSetChanged()
        binding.recycleraddphoto.adapter = addPhotoAdapter
        gamesViewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        gamesViewModel?.personList?.observe(mainActivity) { t ->
            mainActivity.data.clear()
            mainActivity.data.addAll(t as ArrayList<PersonEntity>)
            addPhotoAdapter.notifyDataSetChanged()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun initviews() {
        binding.tvadd.setOnClickListener{
            photopickerdialog()

        }
        binding.btnSave.setOnClickListener {
                if (mainActivity.data.size < 3 || mainActivity.data.any { it.audioRecord.isEmpty() }) {
                    AlertDialog.Builder(mainActivity).apply {
                        setTitle(resources.getString(R.string.sorry))
                        setMessage(
                            if (mainActivity.data.size < 3) {
                                resources.getString(R.string.add_atleast_three_images)
                            } else {
                                resources.getString(R.string.add_audio)
                            }
                        )
                        setCancelable(false)
                        setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                        show()
                    }
                } else {
                    findNavController().navigate(R.id.gameLevelsFragment)
                }

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvadd ->{
               photopickerdialog()

            }
        }
    }

    fun ShowAudioDialog(position: Int=-1){
        println("position $position")
        var dialogBinding = AudioRecordOptionBinding.inflate(layoutInflater)
        dialog = Dialog(mainActivity)
        dialog?.setContentView(dialogBinding.root)
        dialogBinding.startRecord.setOnClickListener {

            recorder = MediaRecorder()
            dirPath = "${mainActivity.externalCacheDir?.absolutePath}/"

            val simpleDateFormat = SimpleDateFormat("DD-MM-yyyy_mm.ss.hh")
            val date = simpleDateFormat.format(Date())
            filename = "audio_record_$date"

            recorder?.apply {
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
//            recorder?.apply {
//                setAudioSource(MediaRecorder.AudioSource.MIC)
//                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                setOutputFile("$dirPath$filename.mp3")
//
//                try {
//                    prepare()
//                    start()
//                } catch (e: IOException) {
//                    println("in exception ${e.toString()}")
//                }
//            }

        }

        dialogBinding.stoprecord.setOnClickListener {
            recorder?.stop()
            try {
                var fos = FileOutputStream("$dirPath$filename")
                var out = ObjectOutputStream(fos)
                out.close()
                fos.close()
            } catch (e: IOException) {

            }
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    for (p in mainActivity.data.indices) {
//                        val updatedAudioPath = "$dirPath$filename.mp3"
//                        gameDB.gameInterface().updatePerson(PersonEntity(audioRecord = updatedAudioPath))
//                    }
//                }
//            }
//            lifecycleScope.launch{
//                for (p in mainActivity.data.indices){
//                    gameDB.gameInterface().updatePerson(PersonEntity(audioRecord = "$dirPath$filename.mp3" ))
//                }
//
//
//            }
//            record.fileName = filename
            audiofile="$dirPath$filename.mp3"

            record.audioRecord = "$dirPath$filename.mp3"
//            record.empsPath = "$dirPath$filename"

//            lifecycleScope.launch{
//                gameDB.gameInterface().updatePerson(record)
//
//            }

//            class saveRecording:  AsyncTask<Void, Void, Void>(){
//                override fun doInBackground(vararg p0: Void?): Void? {
////                    audioDatabase.audioRecordsDao().insert(record)
//                    gameDB.gameInterface().updatePerson(record)
//                    return null
//                }
//
//                override fun onPostExecute(result: Void?) {
//                    super.onPostExecute(result)
//                    dialog.dismiss()
//                    getAudioFiles()
//                }
//            }
//            saveRecording().execute()


//            recorder?.stop()
//            try {
//                var fos = FileOutputStream("$dirPath$filename")
//                var out = ObjectOutputStream(fos)
//                out.close()
//                fos.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "ShowAudioDialog: ${e.message}", )
//            }
//            audiofile = "$dirPath$filename"
//          lifecycleScope.launch {
//              gameDB.gameInterface().updatePerson(PersonEntity(id = position, audioRecord = audiofile))
//              Log.e( TAG, "ShowAudioDialog: $audiofile" )
//          }
        }

        dialogBinding.btnPlayRecordRecord.setOnClickListener {

            if(!mediaPlayer.isPlaying){
                mediaPlayer.reset()
                mediaPlayer.apply {
                    setDataSource(audiofile)
                    prepare()
                    start()
                }
                Log.d("Position State", "$position 1")
//                adapter.updatePosition(position, 1)
            }else{
                mediaPlayer.pause()
//                adapter.updatePosition(position, 0)
            }

        }
        dialogBinding.btnSave.setOnClickListener {
//            if (mainActivity.data.isNullOrEmpty().not()) {
                lifecycleScope.launch {
//                    withContext(Dispatchers.IO) {
//                    val updatedAudioPath = "$dirPath$filename.mp3"
                        personToUpdate.audioRecord = audiofile
                        gameDB.gameInterface().updatePerson(personToUpdate)
                        Toast.makeText(mainActivity, "Recording Saved", Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }
//                gameDB.gameInterface().updatePerson(PersonEntity(audioRecord = record.audioRecord))
                }

//            }
//        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.show()
    }

//    fun getAudioFiles() {
//        viewModelScope.launch {
//            list.clear()
//            list.addAll(audioDatabase.audioRecordsDao().getAllAudioRecords())
//            adapter.notifyDataSetChanged()
//        }
//    }
    private fun photopickerdialog(position: Int = -1) {
        var dialog = Dialog(mainActivity)
        photoDisplayViewItemBinding =  PhotoDisplayViewItemBinding.inflate(layoutInflater)
        dialog.setContentView(photoDisplayViewItemBinding!!.root)
        dialog.setCancelable(true)
        photoDisplayViewItemBinding?.btnAddAudio?.visibility = View.GONE
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        photoDisplayViewItemBinding?.img?.setOnClickListener {
            checkPermissions()
        }

        photoDisplayViewItemBinding?.imgDelete?.setOnClickListener{
            dialog.dismiss()
            galleryUri=null
        }

        if(position == -1){
            photoDisplayViewItemBinding?.img?.setImageResource(R.mipmap.img_place_holder)
        }else{
            photoDisplayViewItemBinding?.img?.setImageURI(Uri.parse(mainActivity.data[position].picture))
        }
        photoDisplayViewItemBinding?.btnaddImage?.setOnClickListener {
            if (
                photoDisplayViewItemBinding?.addname?.text.toString()==""||
                    galleryUri==null
                    ){
                Toast.makeText(mainActivity, "Please add image and name", Toast.LENGTH_SHORT).show()
            }else{
                print("bitmap $bitmap")
                lifecycleScope.launch {
                    if(position > -1){
                        gameDB.gameInterface().updatePerson(PersonEntity(
                            id=mainActivity.data[position].id,
                            picture = galleryUri.toString(),
                            name = photoDisplayViewItemBinding?.addname?.text?.toString()?:""))
                    }else{
                        gameDB.gameInterface().insertPerson(PersonEntity(picture = galleryUri.toString(),
                            name = photoDisplayViewItemBinding?.addname?.text?.toString()?:""))
                    }
                    dialog.dismiss()

                }
            }

        }
        dialog.show()
    }

    companion object {
        // Define the pic id
    }


}