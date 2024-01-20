package com.example.guessthepicture

import android.R.attr
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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


class IntroductionActivity : AppCompatActivity(), View.OnClickListener , CropImageView.OnCropImageCompleteListener{
    lateinit var binding: ActivityIntroductionBinding
    private lateinit var pickerDialog: Dialog
    lateinit var addPhotoAdapter: AddPhotoAdapter
    var data: ArrayList<PersonEntity>?=ArrayList()
    var captureImgUri:Uri?=null
    var imgview:ImageView?=null
    private val PIC_CROP = 101
    var bitmap : Bitmap ? = null
    var view:View?=null
    lateinit var gameDB : GameDB

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this) // optional usage
            captureImgUri=result!!.originalUri
            var galleryUri = result.uriContent
            if(view==imgview){
                imgview?.setImageURI(galleryUri)
            }

            if (view==binding.img1){
                binding.img1.setImageURI(galleryUri)
            }
            else if (view==binding.img2){
                binding.img2.setImageURI(galleryUri)
            }
            else if (view==binding.img3){
                binding.img3.setImageURI(galleryUri)

            }
            else if (view==binding.img4){
                binding.img4.setImageURI(galleryUri)

            }
            else if (view==binding.img4){
                binding.img4.setImageURI(galleryUri)

            }
            else if (view==binding.img5){
                binding.img5.setImageURI(galleryUri)

            }
            else if (view==binding.img6){
                binding.img6.setImageURI(galleryUri)
            }
            else if (view==binding.img7){
                binding.img7.setImageURI(galleryUri)

            }
            else if (view==binding.img8){
                binding.img8.setImageURI(galleryUri)

            }
            else if (view==binding.img9){
                binding.img9.setImageURI(galleryUri)

            }
        } else {
            // An error occurred.
            val exception = result.error
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            captureImgUri?.let { uri ->
                performCrop(uri)
            }
        } else {
            // Handle error
            Toast.makeText(this, "Error capturing image", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{
            if (view==binding.img1){
                binding.img1.setImageURI(galleryUri)
            }
            else if (view==binding.img2){
                binding.img2.setImageURI(galleryUri)
            }
            else if (view==binding.img3){
                binding.img3.setImageURI(galleryUri)

            }
            else if (view==binding.img4){
                binding.img4.setImageURI(galleryUri)

            }
            else if (view==binding.img4){
                binding.img4.setImageURI(galleryUri)

            }
            else if (view==binding.img5){
                binding.img5.setImageURI(galleryUri)

            }
            else if (view==binding.img6){
                binding.img6.setImageURI(galleryUri)

            }
            else if (view==binding.img7){
                binding.img7.setImageURI(galleryUri)

            }
            else if (view==binding.img8){
                binding.img8.setImageURI(galleryUri)

            }
            else if (view==binding.img9){
                binding.img9.setImageURI(galleryUri)

            }

        }catch(e:Exception){
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameDB = GameDB.getDatabase(this)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setSupportActionBar(binding.toolbar)
        initviews()

        lifecycleScope.launch {
            var data = gameDB.gameInterface().getAllPersons()
            println("data from room database  $data")
        }

        binding.recycleraddphoto.layoutManager = LinearLayoutManager(this)
        addPhotoAdapter=AddPhotoAdapter(this, data!!)
        binding.recycleraddphoto.adapter=addPhotoAdapter
    }

    private fun initviews() {
        binding.tvadd.setOnClickListener(this)
        binding.img1.setOnClickListener(this)
        binding.img2.setOnClickListener(this)
        binding.img3.setOnClickListener(this)
        binding.img4.setOnClickListener(this)
        binding.img5.setOnClickListener(this)
        binding.img6.setOnClickListener(this)
        binding.img7.setOnClickListener(this)
        binding.img8.setOnClickListener(this)
        binding.img9.setOnClickListener(this)


        binding.btnSave.setOnClickListener {

                lifecycleScope.launch {
                    gameDB.gameInterface().insertPerson(PersonEntity(picture = bitmap?.let { it1 ->
                        GeneralFunctions.convertBitmapToString(
                            it1
                        )
                    }, name = "Mother"))

                }


            startActivity(Intent(this,GameLevelsActivity::class.java))
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvadd->{
                photopickerdialog()
            }
            R.id.img1 -> {
//                photopickerdialog()
                cropImageLaunch()
                view=binding.img1
            }

            R.id.img2 -> {
//                photopickerdialog()

                cropImageLaunch()
                view=binding.img2
            }

            R.id.img3 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img3
            }
            R.id.img4 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img4
            }
            R.id.img5 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img5
            }
            R.id.img6 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img6
            }
            R.id.img7 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img7
            }
            R.id.img8 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img8
            }
            R.id.img9 -> {
                //                photopickerdialog()

                cropImageLaunch()
                view=binding.img9
            }
        }
    }

    private fun cropImageLaunch() {
        cropImage.launch(CropImageContractOptions(uri = null,
            cropImageOptions = CropImageOptions(
                imageSourceIncludeCamera = true,
                imageSourceIncludeGallery = true,
            ),))
    }
    private fun photopickerdialog() {
        val builder = AlertDialog.Builder(this@IntroductionActivity)
        val inflater = layoutInflater
        val rootView: View = inflater.inflate(R.layout.photo_display_view_item, null)
        val imageset = rootView.findViewById<ImageView>(R.id.img)
        val name = rootView.findViewById<EditText>(R.id.addname)
        val btnadd=rootView.findViewById<Button>(R.id.btnadd)
        builder.setView(rootView)
        builder.setCancelable(true)
        pickerDialog = builder.create()
        val lp2 = WindowManager.LayoutParams()
        val window: Window = pickerDialog.getWindow()!!
        pickerDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        lp2.copyFrom(window.attributes)
        //This makes the dialog take up the full width
        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = lp2
        val dialogWindow: Window = pickerDialog.getWindow()!!
        val lp = dialogWindow.attributes
        dialogWindow.setGravity(Gravity.CENTER)
       imageset.setOnClickListener {
            cropImageLaunch()
            imgview=rootView.findViewById<ImageView>(R.id.img)
            view=imgview
        }
        btnadd.setOnClickListener {

            lifecycleScope.launch {
                gameDB.gameInterface().insertPerson(PersonEntity(picture = bitmap?.let { it1 ->
                    GeneralFunctions.convertBitmapToString(
                        it1
                    )
                }, name = "Mother"))

            }
//            startActivity(Intent(this,GameLevelsActivity::class.java))
        }
        pickerDialog.show()
    }

    private fun choosePhotoFromGallary() {
//        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//       // getImage.launch()
//        startActivityForResult(galleryIntent, GALLERY)
        galleryLauncher.launch("image/*")


    }

    private fun launchcamera() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        try {
//            captureImgUri = createImageFile()
            captureImgUri?.let {
                takePicture.launch(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        takePicture.launch(captureImgUri)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            captureImgUri=data!!.getData()

            val extras: Bundle? = data?.extras
//get the cropped bitmap
//get the cropped bitmap
            val thePic = extras!!.getParcelable<Bitmap>("data")
            binding.img1.setImageBitmap(thePic)

            // BitMap is data structure of image file which store the image in memory
//            val photo = data!!.extras!!["data"] as Bitmap?
//            // Set the image in imageview for display
//            binding.img1.setImageBitmap(photo)
        }
    }

    private fun performCrop(uri: Uri) {
        try {
            val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))

//            CropImage.activity(uri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1, 1) // Set the desired aspect ratio
//                .setOutputCompressQuality(100)
//                .setOutputUri(destinationUri) // Specify the output URI
//                .start(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        // Define the pic id
        val REQUEST_IMAGE_CAPTURE = 100
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        if (result?.isSuccessful == true) {
            val croppedUri = result.uriContent
            // Handle the cropped image URI as needed
            view?.setImageUriAsync(croppedUri)
        } else {
            // Handle the failure case
            Toast.makeText(this, "Error cropping image", Toast.LENGTH_SHORT).show()
        }
    }


}