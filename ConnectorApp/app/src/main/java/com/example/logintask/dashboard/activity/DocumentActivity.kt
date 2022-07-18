package com.example.logintask.dashboard.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.BuildConfig
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.databinding.ActivityDocumentBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.getPreferenceData
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DocumentActivity: BaseActivity() {
    private lateinit var binding :  ActivityDocumentBinding
    private val CAMERA_REQUEST: Int = 1888
    private val PICK_GALLERY_IMAGES_CODE = 0
    private val MY_CAMERA_PERMISSION_CODE = 100
    private var compressedImage: File? = null
    private var cameraFilePath: String? = null
    private var cameraOutURI: Uri? = null
    private val PIC_CROP = 2


    override fun getLayout()= R.layout.activity_document

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {
        overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.toolbar.tvActivityTitle.text = "Document Details"
        binding.btnSubmit.btn.text = "Submit"
        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setOnClickListener(){
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.btnSubmit.btn.setOnClickListener {
            finish()
        }

        val selectedEntity  = getPreferenceData(this,Constant.SELECTED_ENTITY,"")

        when(selectedEntity) {
            "Company(Pvt/Public)" -> {
               binding.llPrivateLtd.visibility = View.VISIBLE
            }
            "Partnership Firm" ->{
                binding.llPartnership.visibility = View.VISIBLE
            }
            "Proprietor" ->{
                binding.llProprietor.visibility = View.VISIBLE
            }
            "Individual" ->{
            }
        }
        binding.ivUploadCustomerPhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "customer photo"
            chooseImageOption()
        }

        binding.ivCancelledChequePhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "cancel cheque photo"
            chooseImageOption()
        }

        binding.ivCustomerPhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self customer photo"
            chooseImageOption()
        }

        binding.ivAddressProofPhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self address photo"
            chooseImageOption()
        }

        binding.ivUploadIdentityProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self identity photo"
            chooseImageOption()
        }

        binding.ivProprietorshipDocument.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship declaration photo"
            chooseImageOption()
        }

        binding.ivUploadGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship gstno photo"
            chooseImageOption()
        }

        binding.ivUploadAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship address photo"
            chooseImageOption()
        }

        binding.ivUploadPartnershipGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership gstno photo"
            chooseImageOption()
        }

        binding.ivUploadPartnershipPan.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership pan photo"
            chooseImageOption()
        }

        binding.ivUploadPartnershipAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership address photo"
            chooseImageOption()
        }

        binding.ivUploadPartnershipSignature.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership signature photo"
            chooseImageOption()
        }

        binding.ivUploadPartnershipDeed.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership deed photo"
            chooseImageOption()
        }

        binding.ivUploadPrivateGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private gstno photo"
            chooseImageOption()
        }

        binding.ivUploadPrivatePan.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private pan no photo"
            chooseImageOption()
        }

        binding.ivUploadPrivateAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private address photo"
            chooseImageOption()
        }

        binding.ivUploadIncorporationCertificate.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private incorporation certificate photo"
            chooseImageOption()
        }

        binding.ivUploadPrivateSignature.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private signature photo"
            chooseImageOption()
        }
    }

    override fun setObserver() {
    }

    override fun setViewModel() {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun chooseImageOption(){

        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(
            R.layout.custom_choose_option_alert_dialog,null)
        builder.setView(view)
        val iv_gallery = view.findViewById<ImageView>(R.id.iv_gallery)
        val iv_camera = view.findViewById<ImageView>(R.id.iv_camera)

        val alertDialog = builder.create()
        alertDialog.show()

        iv_gallery.setOnClickListener {
            openGallery()
            alertDialog.dismiss()
        }

        iv_camera.setOnClickListener {
            openCameraApp()
            alertDialog.dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openCameraApp() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {

            // This is not the right way to do this, but for some reason, having
            // it store it in
            // MediaStore.Images.Media.EXTERNAL_CONTENT_URI isn't working right.
            val date = Date()
            val df = SimpleDateFormat("-mm-ss")

            val newPicFile = "/qr" + df.format(date) + ".jpg"
            val outPath = Environment.getExternalStorageDirectory().toString() + newPicFile
            val outFile = File(outPath)

            cameraFilePath = outFile.toString()
            cameraOutURI = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID.toString() +
                        ".provider", outFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutURI)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }

    private fun openGallery() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto,PICK_GALLERY_IMAGES_CODE)
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageUrl  = data?.data!!
            CropImage.activity(imageUrl)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
           val result = CropImage.getActivityResult(data)
        if (requestCode === CAMERA_REQUEST && resultCode === RESULT_OK) {
            val resultUri = result.uri
            binding.ivDocument.setImageURI(resultUri)
      //    binding.ivDocument.setImageBitmap(data?.extras?.get("data") as Bitmap?)
        }
        } else if (requestCode == PICK_GALLERY_IMAGES_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val contentURI = data.data
                try {

                    Glide.with(this).load(contentURI).placeholder(R.color.gray)
                        .error(R.color.gray).into(binding.ivDocument)
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }

        }*/

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode ==PICK_GALLERY_IMAGES_CODE) {
                performCropLib(data!!.data!!)
                val path = data.data
                val file = File(path?.path)
                val file_size: Int = java.lang.String.valueOf(file.length()/1024).toInt()
                Log.i("Tag",
                    "Image Size before crop = " + file_size
                )
            }else if (requestCode == CAMERA_REQUEST){
                if (cameraFilePath != null && cameraOutURI != null) {
                    val file = File(cameraFilePath)
                    val file1 = File(cameraOutURI!!.path)
                    val file1_size: Int = java.lang.String.valueOf(file.length()/1024).toInt()
                    Log.i("Tag",
                        "Image Size before crop = " + file1_size
                    )
                    performCropLib(cameraOutURI!!)
                }

            } else if (requestCode == PIC_CROP && data != null) {
                Log.e("CROP", data.extras.toString())
                val extras = data.extras
                val selectedBitmap = extras!!.getParcelable<Bitmap>("data")
            } else
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == RESULT_OK) {
                        val resultUri = result.uri
                        val fileName = File(resultUri.path)
                        customCompressImage(fileName)
                        Log.i("Tag",
                            "onActivityResult: Crop URI = " + resultUri.path
                        )

                        val file = File(resultUri.path)
                        val file_size: Int = java.lang.String.valueOf(file.length()/1024).toInt()
                        Log.i("Tag",
                            "Image Size after crop = " + file_size
                        )

                        when(Constant.UPLOAD_DOCUMENT) {
                            "customer photo" -> {
                                binding.ivDocument.setImageURI(resultUri)
                            }
                            "cancel cheque photo" ->{
                                binding.ivCancelledCheque.setImageURI(resultUri)
                            }
                            "self customer photo" ->{
                                binding.ivSelfCustomerPhoto.setImageURI(resultUri)
                            }
                            "self address photo" ->{
                                binding.ivAddressProof.setImageURI(resultUri)
                            }
                            "self identity photo" ->{
                                binding.ivIdentityProof.setImageURI(resultUri)
                            }
                            "proprietorship declaration photo" ->{
                                binding.ivProprietorshipDeclaration.setImageURI(resultUri)
                            }
                            "proprietorship gstno photo" ->{
                                binding.ivGstinNo.setImageURI(resultUri)
                            }
                            "proprietorship address photo" ->{
                                binding.ivProprientorAddressProof.setImageURI(resultUri)
                            }
                            "partnership gstno photo" ->{
                                binding.ivPartnershipGstinNo.setImageURI(resultUri)
                            }
                            "partnership pan photo" ->{
                                binding.ivPartnershipPanNo.setImageURI(resultUri)
                            }
                            "partnership address photo" ->{
                                binding.ivPartnershipAddressProof.setImageURI(resultUri)
                            }
                            "partnership signature photo"->{
                                binding.ivSignature.setImageURI(resultUri)
                            }
                            "partnership deed photo" ->{
                                binding.ivPartnershipDeed.setImageURI(resultUri)
                            }
                            "private gstno photo" ->{
                                binding.ivPrivateGstinNo.setImageURI(resultUri)
                            }
                            "private pan no photo" ->{
                                binding.ivPrivatePanNo.setImageURI(resultUri)
                            }
                            "private address photo" ->{
                                binding.ivPrivateAddressProof.setImageURI(resultUri)
                            }
                            "private incorporation certificate photo"->{
                                binding.ivCertificateIncorparation.setImageURI(resultUri)
                            }
                            "private signature photo" ->{
                                binding.ivPrivateSignature.setImageURI(resultUri)
                            }

                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        val error = result.error
                    }
                }
        }
    }


    private fun performCropLib(uri: Uri) {
// start cropping activity for pre-acquired image saved on the device
        Log.i("Tag", "performCropLib: URI = $uri")
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setAllowRotation(true)
            .setAutoZoomEnabled(true)
            .setActivityTitle("Crop Image")
            .start(this)
    }

    fun customCompressImage(fileName: File) {
        fileName?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(this@DocumentActivity, imageFile) {
                    resolution(200, 200)
                    quality(80)
                    format(Bitmap.CompressFormat.PNG)
                    size( 51200) // 50kb

                }
                val file = File(compressedImage!!.path)
                val i = file.length()
                Log.d("image", i.toString())
            }
        }
    }

        override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
           MY_CAMERA_PERMISSION_CODE-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }

            PICK_GALLERY_IMAGES_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }
            }

        }
    }
}
