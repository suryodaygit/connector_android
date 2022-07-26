package com.example.logintask.dashboard.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.BuildConfig
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.databinding.ActivityDocumentBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File


class DocumentActivity: BaseActivity() {
    private lateinit var binding :  ActivityDocumentBinding
    private var compressedImage: File? = null
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

        val imageUrl = getPreferenceData(this, Constant.PROFILE_IMAGE,"").toUri()
        binding.ivDocument.setImageURI(imageUrl)
        setDataInPreference(
            this@DocumentActivity,
            Constant.CUSTOMER_PHOTO,
          imageUrl.toString()
        )

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
            chooseImageOption(this)
        }

        binding.ivCancelledChequePhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "cancel cheque photo"
            chooseImageOption(this)
        }

        binding.ivCustomerPhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self customer photo"
            chooseImageOption(this)
        }

        binding.ivAddressProofPhoto.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self address photo"
            chooseImageOption(this)
        }

        binding.ivUploadIdentityProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self identity photo"
            chooseImageOption(this)
        }

        binding.ivUploadAadharFront.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self aadhar front photo"
            chooseImageOption(this)
        }

        binding.ivUploadAadharBack.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "self aadhar back photo"
            chooseImageOption(this)
        }


        binding.ivProprietorshipDocument.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship declaration photo"
            chooseImageOption(this)
        }

        binding.ivUploadGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship gstno photo"
            chooseImageOption(this)
        }

        binding.ivUploadAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "proprietorship address photo"
            chooseImageOption(this)
        }

        binding.ivUploadPartnershipGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership gstno photo"
            chooseImageOption(this)
        }

        binding.ivUploadPartnershipPan.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership pan photo"
            chooseImageOption(this)
        }

        binding.ivUploadPartnershipAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership address photo"
            chooseImageOption(this)
        }

        binding.ivUploadPartnershipSignature.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership signature photo"
            chooseImageOption(this)
        }

        binding.ivUploadPartnershipDeed.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "partnership deed photo"
            chooseImageOption(this)
        }

        binding.ivUploadPrivateGstinNo.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private gstno photo"
            chooseImageOption(this)
        }

        binding.ivUploadPrivatePan.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private pan no photo"
            chooseImageOption(this)
        }

        binding.ivUploadPrivateAddressProof.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private address photo"
            chooseImageOption(this)
        }

        binding.ivUploadIncorporationCertificate.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private incorporation certificate photo"
            chooseImageOption(this)
        }

        binding.ivUploadPrivateSignature.setOnClickListener {
            Constant.UPLOAD_DOCUMENT = "private signature photo"
            chooseImageOption(this)
        }
    }

    override fun setObserver() {
    }

    override fun setViewModel() {
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
                val imageFile = File(Constant.mCurrentPhotoPath!!)
                performCropLib(imageFile.toUri())

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
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.CUSTOMER_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "cancel cheque photo" ->{
                                binding.ivCancelledCheque.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.CANCELLED_CHEQUE,
                                    resultUri.toString()
                                )
                            }
                            "self address photo" ->{
                                binding.ivAddressProof.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.CUSTOMER_ADDRESS_PROOF,
                                    resultUri.toString()
                                )
                            }
                            "self identity photo" ->{
                                binding.ivIdentityProof.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.IDENTITY_PROOF,
                                    resultUri.toString()
                                )
                            }
                            "self aadhar front photo" ->{
                                binding.ivAadharFront.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.SELF_AADHAR_FRONT,
                                    resultUri.toString()
                                )
                            }

                            "self aadhar back photo" ->{
                                binding.ivAadharBack.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.SELF_AADHAR_BACK,
                                    resultUri.toString()
                                )
                            }
                            "proprietorship declaration photo" ->{
                                binding.ivProprietorshipDeclaration.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PROPRIENTORSHIP_DECLARATION_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "proprietorship gstno photo" ->{
                                binding.ivGstinNo.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PROPRIENTORSHIP_GSTNO_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "proprietorship address photo" ->{
                                binding.ivProprientorAddressProof.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PROPRIENTORSHIP_ADDRESS_PROOF,
                                    resultUri.toString()
                                )
                            }
                            "partnership gstno photo" ->{
                                binding.ivPartnershipGstinNo.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PARTNERSHIP_GSTNO_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "partnership pan photo" ->{
                                binding.ivPartnershipPanNo.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PARTNERSHIP_PAN_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "partnership address photo" ->{
                                binding.ivPartnershipAddressProof.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PARTNERSHIP_ADDRESS_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "partnership signature photo"->{
                                binding.ivSignature.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PARTNERSHIP_SIGNATURE_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "partnership deed photo" ->{
                                binding.ivPartnershipDeed.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PARTNERSHIP_DEED_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "private gstno photo" ->{
                                binding.ivPrivateGstinNo.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PRIVATE_GST_NO_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "private pan no photo" ->{
                                binding.ivPrivatePanNo.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PRIVATE_PAN_NO_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "private address photo" ->{
                                binding.ivPrivateAddressProof.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PRIVATE_ADDRESS_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "private incorporation certificate photo"->{
                                binding.ivCertificateIncorparation.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PRIVATE_INCORPORATION_CERTIFICATE_PHOTO,
                                    resultUri.toString()
                                )
                            }
                            "private signature photo" ->{
                                binding.ivPrivateSignature.setImageURI(resultUri)
                                setDataInPreference(
                                    this@DocumentActivity,
                                    Constant.PRIVATE_SIGNATURE_PHOTO,
                                    resultUri.toString()
                                )
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
                    openGallery(this)
                }
            }

        }
    }
}
