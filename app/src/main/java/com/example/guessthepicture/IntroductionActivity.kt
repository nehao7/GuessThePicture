package com.example.guessthepicture

import android.R.attr
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.PersonEntity
import kotlinx.coroutines.launch
import java.io.File
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.guessthepicture.adapters.AddPhotoAdapter
import com.example.guessthepicture.databinding.ActivityIntroductionBinding
import com.example.guessthepicture.databinding.PhotoDisplayViewItemBinding


class IntroductionActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var binding: ActivityIntroductionBinding
    private lateinit var pickerDialog: Dialog
    lateinit var addPhotoAdapter: AddPhotoAdapter
    var data= ArrayList<PersonEntity>()
    var captureImgUri:Uri?=null
    var bitmap : Bitmap ? = null
    var view:View?=null
    lateinit var gameDB : GameDB
    var photoDisplayViewItemBinding: PhotoDisplayViewItemBinding ?= null
    private val TAG = "IntroductionActivity"

//    val rootView =  PhotoDisplayViewItemBinding.inflate(layoutInflater)

    private var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            cropImage.launch(CropImageContractOptions(uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = false,
                    imageSourceIncludeGallery = true,
                ),))
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this) // optional usage
            captureImgUri=result!!.originalUri
            var galleryUri = result.uriContent
            bitmap = result.bitmap
            Log.e(TAG, " bitmap $bitmap ${result.bitmap} $result captureImgUri $captureImgUri $galleryUri $uriContent")
            photoDisplayViewItemBinding?.img?.setImageURI(galleryUri)

        } else {
            // An error occurred.
            val exception = result.error
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameDB = GameDB.getDatabase(this)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setSupportActionBar(binding.toolbar)
        initviews()
        getSupportActionBar()?.hide()

        binding.recycleraddphoto.layoutManager = LinearLayoutManager(this)
        addPhotoAdapter=AddPhotoAdapter(this, data)
        binding.recycleraddphoto.adapter=addPhotoAdapter

        lifecycleScope.launch {
            data = gameDB.gameInterface().getAllPersons() as ArrayList<PersonEntity>
            println("data from room database  $data")
            addPhotoAdapter.notifyDataSetChanged()
        }
    }

    private fun initviews() {
        binding.tvadd.setOnClickListener(this)


        binding.btnSave.setOnClickListener {
            startActivity(Intent(this,GameLevelsActivity::class.java))
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvadd->{
                photopickerdialog()
            }

        }
    }

    private fun cropImageLaunch() {

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            cropImage.launch(CropImageContractOptions(uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = false,
                    imageSourceIncludeGallery = true,
                ),))
        }else{
            getPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    private fun photopickerdialog() {
        var dialog = Dialog(this)
        photoDisplayViewItemBinding =  PhotoDisplayViewItemBinding.inflate(layoutInflater)
        dialog.setContentView(photoDisplayViewItemBinding!!.root)
        dialog.setCancelable(true)
        val lp2 = WindowManager.LayoutParams()
        val window: Window = dialog.getWindow()!!
        //This makes the dialog take up the full width
        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = lp2
        val dialogWindow: Window = dialog.getWindow()!!
        dialogWindow.setGravity(Gravity.CENTER)
        photoDisplayViewItemBinding?.img?.setOnClickListener {
            cropImageLaunch()

        }
        photoDisplayViewItemBinding?.btnaddImage?.setOnClickListener {

            lifecycleScope.launch {
                gameDB.gameInterface().insertPerson(PersonEntity(picture = bitmap?.let { it1 ->
                    GeneralFunctions.convertBitmapToString(
                        it1
                    )
                }, name = "Mother"))
            }
            lifecycleScope.launch {
                data.addAll(gameDB.gameInterface().getAllPersons())
            }

           // startActivity(Intent(this,GameLevelsActivity::class.java))
        }
        dialog.show()
    }

    companion object {
        // Define the pic id
    }


}