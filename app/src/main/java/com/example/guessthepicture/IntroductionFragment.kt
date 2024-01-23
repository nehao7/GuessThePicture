package com.example.guessthepicture

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.guessthepicture.databinding.FragmentIntroductionBinding

class IntroductionFragment : Fragment(), View.OnClickListener{
    lateinit var binding: FragmentIntroductionBinding
    lateinit var addPhotoAdapter: AddPhotoAdapter
    var captureImgUri:Uri?=null
    var bitmap : Bitmap ? = null
    lateinit var gameDB : GameDB
    var galleryUri:Uri?=null
    var photoDisplayViewItemBinding: PhotoDisplayViewItemBinding ?= null
    private val TAG = "IntroductionFragment"
    lateinit var mainActivity: MainActivity
    var gamesViewModel : GamesViewModel ?= null
    var permission = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S)
        Manifest.permission.READ_EXTERNAL_STORAGE
    else{
        Manifest.permission.READ_MEDIA_IMAGES
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getPermission.launch(permission)
        } else{
            launchCropImage()
        }
    }

    private var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            launchCropImage()
        }
    }

    fun launchCropImage(){
        cropImage.launch(CropImageContractOptions(uri = null,
            cropImageOptions = CropImageOptions(
                imageSourceIncludeCamera = false,
                imageSourceIncludeGallery = true,
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
        binding.recycleraddphoto.layoutManager = GridLayoutManager(mainActivity,3)
        addPhotoAdapter = AddPhotoAdapter(mainActivity,mainActivity.data,
            object :AddPhotoAdapter.ViewHandler{
                override fun viewHandler(position: ImageView) {

                }

            })
        addPhotoAdapter.notifyDataSetChanged()
        binding.recycleraddphoto.adapter = addPhotoAdapter

        gamesViewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        gamesViewModel?.personList?.observe(mainActivity
        ) { t ->
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
        binding.tvadd.setOnClickListener(this)

        binding.btnSave.setOnClickListener {
            if(mainActivity.data.size < 3){
                AlertDialog.Builder(mainActivity).apply {
                    setTitle(resources.getString(R.string.sorry))
                    setMessage(resources.getString(R.string.add_atleast_three_images))
                    setCancelable(false)
                    setPositiveButton(resources.getString(R.string.ok), {_,_->})
                }
            }else{
                findNavController().navigate(R.id.gameLevelsFragment)

            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvadd->{
               photopickerdialog()
            }

        }
    }

    private fun photopickerdialog() {
        var dialog = Dialog(mainActivity)
        photoDisplayViewItemBinding =  PhotoDisplayViewItemBinding.inflate(layoutInflater)
        dialog.setContentView(photoDisplayViewItemBinding!!.root)
        dialog.setCancelable(true)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        photoDisplayViewItemBinding?.img?.setOnClickListener {
            checkPermissions()

        }
        photoDisplayViewItemBinding?.btnaddImage?.setOnClickListener {
        print("bitmap $bitmap")
            lifecycleScope.launch {
                gameDB.gameInterface().insertPerson(PersonEntity(picture = galleryUri.toString()
                , name = "Mother"))
                dialog.dismiss()

            }
        }
        dialog.show()
    }

    companion object {
        // Define the pic id
    }


}