package com.example.guessthepicture

import android.Manifest
import android.R.attr
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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
    var imgid=0
    lateinit var gameDB : GameDB
    var media_request_code=101
    var galleryUri:Uri?=null
    var photoDisplayViewItemBinding: PhotoDisplayViewItemBinding ?= null
    private val TAG = "IntroductionActivity"

//    val rootView =  PhotoDisplayViewItemBinding.inflate(layoutInflater)

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val requiredPermissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            val permissionsToRequest = mutableListOf<String>()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(permission)
                }
            }

            if (permissionsToRequest.isNotEmpty()) {
                // Request the permissions
                ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toTypedArray(),
                    media_request_code
                )
            } else {

            }
        } else {
            // Permissions are granted at install time on versions prior to Android 6.0
            // Do something here
        }
    }

    private var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            cropImage.launch(CropImageContractOptions(uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = true,
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
            galleryUri = result.uriContent
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
        checkPermissions()
        initviews()
        getSupportActionBar()?.hide()

//        binding.recycleraddphoto.layoutManager = LinearLayoutManager(this)
//        addPhotoAdapter=AddPhotoAdapter(this, data)
//        binding.recycleraddphoto.adapter=addPhotoAdapter

        binding.recycleraddphoto.layoutManager = GridLayoutManager(applicationContext,3)
        addPhotoAdapter = AddPhotoAdapter(this,data,
            object :AddPhotoAdapter.ViewHandler{
                override fun viewHandler(position: ImageView) {
                    for (a in data.indices){
                        position.setImageURI(Uri.parse(data[a].picture))
                    }
//                addPhotoAdapter.notifyDataSetChanged()
                }

            })
        addPhotoAdapter.notifyDataSetChanged()
        binding.recycleraddphoto.adapter = addPhotoAdapter


        lifecycleScope.launch {
            data = gameDB.gameInterface().getAllPersons() as ArrayList<PersonEntity>
            println("data from room database  $data")
            addPhotoAdapter.notifyDataSetChanged()
        }
    }

    private fun initviews() {
        binding.tvadd.setOnClickListener(this)

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                data.add(PersonEntity(imgid,galleryUri.toString(),"Mother"))
                Log.e("entitylist", "initviews: "+data )
            }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cropImage.launch(
                    CropImageContractOptions(
                        uri = null,
                        cropImageOptions = CropImageOptions(
                            imageSourceIncludeCamera = true,
                            imageSourceIncludeGallery = true,
                        ),
                    )
                )
            } else {
                getPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
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
        print("bitmap $bitmap")
//            lifecycleScope.launch {
//                gameDB.gameInterface().insertPerson(PersonEntity(picture = galleryUri.toString()
//                , name = "Mother"))
//            }

            lifecycleScope.launch {
                gameDB.gameInterface().insertPerson(PersonEntity(picture = galleryUri.toString()
                , name = "Mother"))
                for (i in data.indices){
                  imgid=data[i].id
                }
                data.add(PersonEntity(imgid,galleryUri.toString(),"Mother"))
                Log.e("entitylist", "initviews: "+data )


            }



            // startActivity(Intent(this,GameLevelsActivity::class.java))
        }
        dialog.show()
    }

    companion object {
        // Define the pic id
    }


}