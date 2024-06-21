package com.app.mvvm.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.mvvm.BuildConfig
import com.app.mvvm.MyApplication
import com.app.mvvm.R
import com.app.mvvm.databinding.ActivityMainBinding
import com.app.mvvm.databinding.DialogCameraBinding
import com.app.mvvm.model.viewModel.MainViewModel
import com.app.mvvm.util.CAMERA_PERMISSION
import com.app.mvvm.util.FileUtil
import com.app.mvvm.util.GALLERY_PERMISSION
import com.app.mvvm.util.NetworkResult
import com.app.mvvm.util.PreferenceManager
import com.app.mvvm.util.UtilJ.getImageType
import com.app.mvvm.util.Utils
import com.app.mvvm.util.checkSelfPermissions
import com.app.mvvm.util.hasPermissions
import com.app.mvvm.util.shouldShow
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val b: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()
    lateinit var pref: PreferenceManager

    private lateinit var dialogBinding: DialogCameraBinding
    private lateinit var dialog: AlertDialog
    private lateinit var currentPhotoPath: String
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(b.root)
        extra()
        click()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun extra() {
        pref = PreferenceManager(this)
        utils = Utils(this)
        fetchData()
        initDialog()
    }


    private fun click() {
        b.imgMain.setOnClickListener {
            dialog.show()
        }
        b.country.setOnClickListener {
            startActivity(Intent(this, CountryActivity::class.java))
        }
        b.list.setOnClickListener {
            startActivity(Intent(this, EventActivity::class.java))
        }
    }

    private fun initDialog() {
        dialogBinding = DialogCameraBinding.inflate(layoutInflater)

        dialog = AlertDialog.Builder(this@MainActivity).create()
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialog.setView(dialogBinding.root)

        dialogBinding.llGallery.setOnClickListener {
            if (hasPermissions(this@MainActivity, GALLERY_PERMISSION)) {
                openGalleryLauncherIntent()
            } else {
                permGalleryLauncher.launch(GALLERY_PERMISSION)
            }
            dialog.dismiss()
        }

        dialogBinding.llCamera.setOnClickListener {
            if (hasPermissions(this@MainActivity, CAMERA_PERMISSION)) {
                openCameraLauncherIntent()
            } else {
                permCameraLauncher.launch(CAMERA_PERMISSION)
            }
            dialog.dismiss()
        }

        dialogBinding.ivCloseDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun fetchData() {
        mainViewModel.fetchDogResponse()

        mainViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    MyApplication.progressBar.dismiss()
                    Log.e("TAG", "fetchData: " + response.data?.status)
                    Log.e("TAG", "fetchData: " + response.data?.message)
                }

                is NetworkResult.Error -> {
                    // Handle the error case
                    MyApplication.progressBar.dismiss()
                }

                is NetworkResult.Loading -> {
                    // Handle the loading case
                    MyApplication.progressBar.show(this)
                }

                else -> {
                    // Handle other cases
                    MyApplication.progressBar.dismiss()
                }
            }
        }

        mainViewModel.mediaResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    MyApplication.progressBar.dismiss()
                    Log.e("TAG", "fetchData: " + response.data?.status)
                    Log.e("TAG", "fetchData: " + response.data?.message)
                }

                is NetworkResult.Error -> {
                    // Handle the error case
                    MyApplication.progressBar.dismiss()
                }

                is NetworkResult.Loading -> {
                    // Handle the loading case
                    MyApplication.progressBar.show(this)
                }

                else -> {
                    // Handle other cases
                    MyApplication.progressBar.dismiss()
                }
            }
        }
    }

    //Permission Camera
    private val permCameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                openCameraLauncherIntent()
            } else {
                if (shouldShow(this@MainActivity, CAMERA_PERMISSION)) {

                } else {
                    val snackBar = Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.permission_text),
                        Snackbar.LENGTH_LONG
                    ).setAction("Settings") {
                        settingCameraLauncher.launch(
                            Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                            )
                        )
                    }
                    val snackBarView = snackBar.view
                    val textView =
                        snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.maxLines = 5
                    snackBar.show()
                }
            }
        }

    private val permGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                openGalleryLauncherIntent()
            } else {
                if (shouldShow(this@MainActivity, GALLERY_PERMISSION)) {

                } else {
                    val snackBar = Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.permission_text),
                        Snackbar.LENGTH_LONG
                    ).setAction("Settings") {
                        settingGalleryLauncher.launch(
                            Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                            )
                        )
                    }
                    val snackBarView = snackBar.view
                    val textView =
                        snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.maxLines = 5  //Or as much as you need
                    snackBar.show()
                }
            }
        }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val tempUri = Uri.fromFile(File(currentPhotoPath))
                UCrop.of(tempUri!!, Uri.fromFile(File(cacheDir, System.currentTimeMillis().toString() + "Crop_Image.jpg")
                    )
                ).withAspectRatio(9F, 16F).start(this)
            }

        }
    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val selectedImage: Uri? = result.data!!.data
                    UCrop.of(
                        selectedImage!!, Uri.fromFile(
                            File(
                                cacheDir, System.currentTimeMillis().toString() + "Crop_Image.jpg"
                            )
                        )
                    ).withAspectRatio(9F, 16F).start(this)
                }
            }
        }

    private var settingGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (checkSelfPermissions(this, GALLERY_PERMISSION)) {
                openGalleryLauncherIntent()
            }
        }

    private var settingCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (checkSelfPermissions(this, CAMERA_PERMISSION)) {
                openCameraLauncherIntent()
            }
        }


    private fun openGalleryLauncherIntent() {
        val pictureActionIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pictureActionIntent)
    }

    private fun openCameraLauncherIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                        applicationContext, applicationContext.packageName + ".provider", photoFile
                    )
                )
                cameraLauncher.launch(takePictureIntent)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val temp = FileUtil.getPath(this, resultUri!!)
            Log.e("TAG", "onActivityResult: " + temp)

            mainViewModel.mediaUpload("1",getImageType(File(temp)))

            Glide.with(this).load(temp).into(b.imgMain)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            utils.showToast(cropError.toString())
        }
    }


}
