package com.cmrk.ui.activity.startTrip

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.cmrk.MainActivity
import com.cmrk.R
import com.cmrk.databinding.ActivityStartTripBinding
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.StartTripResponseModel
import com.cmrk.ui.activity.responseModel.EndTripResponseModel
import com.cmrk.ui.activity.visitor_detail.VisitorPersonAreaDetailActivity
import com.cmrk.ui.profile.DashboardResponseModel
import com.cmrk.util.*
import com.cmrk.util.Controller.Companion.ISFROM
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class StartTripActivity : AppCompatActivity(), NetworkCallBack {
    private lateinit var activityStartTripBinding: ActivityStartTripBinding
    private var mCurrentPhotoPath: String? = null
    var bundle: Bundle? = null
    var dashboardResponseModel: DashboardResponseModel? = null
    private var actualImage: File? = null
    private var compressedImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStartTripBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_start_trip)

        activityStartTripBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityStartTripBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityStartTripBinding.toolbar.tvActivityTitle.text =
            this.resources?.getString(R.string.start_trip_str)

        activityStartTripBinding.txtDate.text = this.resources?.getString(R.string.date_str)
            .plus(" ").plus(DateUtils.getCurrentDate(Calendar.getInstance().time))

        activityStartTripBinding.imgClickPhoto.setOnClickListener {
            if (!PermissionHelper.checkPermission(
                    this,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) || !PermissionHelper.checkPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ){
                PermissionHelper.askPermission(
                    this@StartTripActivity, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ),
                    REQUEST_CODE_STORAGE_CAMERA_PERMISSION
                )
            } else {
                openCameraApp()
            }
        }
        activityStartTripBinding.txtShowLocation.setOnClickListener {
            when {
                PermissionHelper.isAccessFineLocationGranted(this) -> {
                    when {
                        PermissionHelper.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionHelper.showGPSNotEnabledDialog(this)
                        }
                    }
                }
                else -> {
                    PermissionHelper.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
        activityStartTripBinding.btnStartTrip.setOnClickListener {
            if (activityStartTripBinding.edtStartKm.text.isEmpty()) {
                Toaster.showShortToast(
                    this,
                    this@StartTripActivity.resources.getString(R.string.start_km_validation)
                )
            } else if (mCurrentPhotoPath == null) {
                Toaster.showShortToast(
                    this,
                    this@StartTripActivity.resources.getString(R.string.odometer_image_capture_validation)
                )
            } else if (activityStartTripBinding.txtShowLocation.text.toString() == "Show Location") {
                Toaster.showShortToast(
                    this,
                    this@StartTripActivity.resources.getString(R.string.location_validation)
                )
            } else {
                if (dashboardResponseModel?.data?.tripId.equals(null) || dashboardResponseModel?.data?.tripId.equals(
                        "null"
                    )
                ) {
                    startTrip()
                } else {
                    callEndTripAPI()
                }
            }
        }
    }

    private fun callEndTripAPI() {
        LoadingDialog.showLoading(this@StartTripActivity)
        val currentDateandTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(this@StartTripActivity, true).apiRequest(
                ApiProvider.provideApi(this@StartTripActivity).doEndTripAsync(
                    AppPreference.loadLoginData(this@StartTripActivity).data?.employeeKeyId.toString(),
                    activityStartTripBinding.edtStartKm.text.toString(),
                    Latitude,
                    Longitude,
                    currentDateandTime,
                    dashboardResponseModel?.data?.tripKeyId.toString(),
                    uploadedFileName
                ),
                this@StartTripActivity
            )
        }
    }

    private fun startTrip() {
        LoadingDialog.showLoading(this@StartTripActivity)
        val date  =  DateUtils.getCurrentDateWithSimpleDateFormat(Calendar.getInstance().time)
            .toString()

        val currentDateandTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(this@StartTripActivity, true).apiRequest(
                ApiProvider.provideApi(this@StartTripActivity).doStartTripAsync(
                   AppPreference.loadLoginData(this@StartTripActivity).data?.employeeKeyId.toString(),
                    activityStartTripBinding.edtStartKm.text.toString(),
                    Latitude,
                    Longitude,
                    currentDateandTime,
                    uploadedFileName
                ),
                this@StartTripActivity
            )
        }
    }

    private fun openCameraApp() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this!!,
            "${this?.packageName}.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    @Throws(IOException::class)
    private fun createFile(): File {
        val storageDir: File = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp = System.currentTimeMillis()
        return File.createTempFile("IMG_$timeStamp" + "_DOC", ".jpg", storageDir).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCameraApp()
                } else {
                    if (!PermissionHelper.showRational(this!!, permissions[0])) {
                        ShowRationPermissionDialog.createDialog(
                            this,
                            getString(R.string.rational_camera),
                            object :
                                ShowRationPermissionDialog.ShowRationPermissionDialogButtonClick {
                                override fun openAppSetting() {

                                }
                            }
                        )
                        return
                    }

                    if (!PermissionHelper.showRational(this!!, permissions[1])) {
                        ShowRationPermissionDialog.createDialog(
                            this!!,
                            getString(R.string.rational_storage_camera),
                            object :
                                ShowRationPermissionDialog.ShowRationPermissionDialogButtonClick {
                                override fun openAppSetting() {

                                }

                            }

                        )
                        return
                    }
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionHelper.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionHelper.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun customCompressImage(fileName:File) {
        fileName?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(this@StartTripActivity, imageFile) {
                    resolution(200, 200)
                    quality(80)
                    format(Bitmap.CompressFormat.PNG)
                    size(2_097_152) // 2 MB
                }
                val file = File(compressedImage!!.path)
                val i = file.length()
                Controller.OdoMeterFile = file
                uploadOdomoterPhoto()
                Log.d("image",compressedImage.toString())
                // setCompressedImage()
            }
        }  //showError("Please choose an image!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageFile = File(mCurrentPhotoPath!!)
            if (imageFile.exists()) {
               // Controller.OdoMeterFile = imageFile
             customCompressImage(imageFile)
//                Controller.OdoMeterFile = File("/storage/emulated/0/Android/data/com.cmrk/files/Pictures/demo.jpeg")
                val myBitmapPath =
                    BitmapUtils.getCompressedImage(
                        this,
                        mCurrentPhotoPath!!,
                        ImageView.ScaleType.FIT_CENTER
                    )
                val bitmapImage = BitmapFactory.decodeFile(mCurrentPhotoPath!!)
                val nh = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true)
//                Controller.myBitmap =
//                    Controller.timestampItAndSave(BitmapFactory.decodeFile(myBitmapPath))
                Controller.saveBitmap(
                    BitmapFactory.decodeFile(
                        myBitmapPath
                    ), this
                )

                activityStartTripBinding.imgShowCapturedPhoto.visibility = View.VISIBLE
                activityStartTripBinding.imgClickPhoto.visibility = View.GONE
                activityStartTripBinding.imgShowCapturedPhoto.setImageBitmap(scaled)
//                uploadImage()
               // uploadOdomoterPhoto()
            }
        }
    }

    private fun uploadOdomoterPhoto() {
        val requestFile: RequestBody =
            RequestBody.Companion.create(
                "multipart/form-data".toMediaTypeOrNull(),
                Controller.OdoMeterFile
            )
        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", Controller.OdoMeterFile.name, requestFile)
        // add another part within the multipart request
        LoadingDialog.showLoading(this@StartTripActivity)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(this@StartTripActivity, true).apiRequest(
                ApiProvider.provideApi(this@StartTripActivity).addImage(body),
                this@StartTripActivity
            )
        }

//        GlobalScope.launch(Dispatchers.Main) {
//            ApiRequest(context, true).apiRequest(
//                ApiProvider.provideApi(context).addSurveyTypeImage(surveyId, surveyTypeID, body),
//                this@PreviewImageViewModel
//            )
//        }

//        for (i in 0 until Controller.SelectedImageEncodedList.size) {
//            Controller.filename = "file$i"
//            imagesEncodedList = Controller.SelectedImageEncodedList[i]
//            file = File(imagesEncodedList)
//        }
//
//        val bodyBuilder = MultipartBody.Builder()
//        val builder: MultipartBody.Builder = bodyBuilder.setType(MultipartBody.FORM)
//        for (i in 0 until Controller.SelectedImageEncodedList.size) {
//            Controller.filename = "file[$i]"
//            imagesEncodedList = Controller.SelectedImageEncodedList[i]
//            var imageFile = File(Controller.SelectedImageEncodedList[i])
//            file = File(imagesEncodedList)
////            builder.addFormDataPart("file", RequestBody.Companion.create(
////                "".toMediaTypeOrNull(),
////                imageFile
////            ))
//
//
//            val file: File = File(Controller.SelectedImageEncodedList[i])
//
//            val fbody: RequestBody = RequestBody.Companion.create(
//                "".toMediaTypeOrNull(),
//                file
//            )
//
//            LoadingDialog.showLoading(this)
//            GlobalScope.launch(Dispatchers.Main) {
//                ApiRequest(this@StartTripActivity, true).apiRequest(
//                    apiprovider.provideapi(this@starttripactivity).uploadimage(fbody),
//                    this@StartTripActivity
//                )
//            }
//        }

//        builder.build()
//
//        val documentbodyreq: RequestBody = builder.build()
//        LoadingDialog.showLoading(this)
//        GlobalScope.launch(Dispatchers.Main) {
//            ApiRequest(this@StartTripActivity, true).apiRequest(
//                apiprovider.provideapi(this@starttripactivity).uploadimagefile(documentbodyreq),
//                this@StartTripActivity
//            )
//        }

    }

    override fun onStart() {
        super.onStart()
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            Latitude = location.latitude.toString()
                            Longitude = location.longitude.toString()
                            activityStartTripBinding.txtShowLocation.text =
                                "Latitude : " + location.latitude.toString() + "\n" +
                                        "Longitude : " + location.longitude.toString()
                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                it
            )
        }
    }

    override fun onResume() {
        super.onResume()
        bundle = this.intent.extras
        dashboardResponseModel = bundle?.getSerializable("DashboardData") as DashboardResponseModel?
        if (dashboardResponseModel?.data?.tripId.equals(null) || dashboardResponseModel?.data?.tripId.equals(
                "null"
            )
        ) {
            activityStartTripBinding.txtTripId.visibility = View.GONE
            activityStartTripBinding.edtStartKm.hint =
                this.resources.getString(R.string.Start_km_str)
            activityStartTripBinding.toolbar.tvActivityTitle.text =
                this.resources?.getString(R.string.start_trip_str)
            activityStartTripBinding.edtStartKm.hint =
                this.resources.getString(R.string.start_trip_str)
        } else {
            activityStartTripBinding.txtTripId.visibility = View.VISIBLE
            activityStartTripBinding.txtTripId.text =
                "Trip ID : ".plus(dashboardResponseModel?.data?.tripId.toString())
            activityStartTripBinding.toolbar.tvActivityTitle.text =
                this.resources?.getString(R.string.end_trip_str)
            activityStartTripBinding.edtStartKm.hint = this.resources.getString(R.string.End_km_str)
            activityStartTripBinding.btnStartTrip.text =
                this.resources.getString(R.string.btn_end_trip)
        }
    }

    companion object {
        var imagesEncodedList: String = ""
        lateinit var file: File
        lateinit var uploadedFileName: String
        var Latitude: String = ""
        var Longitude: String = ""
    }

    override fun onSuccess(response: Any?) {
        if (response is UploadImageFileResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showLongToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                }
                1 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    uploadedFileName = response.data?.file.toString()
                    LoadingDialog.hideLoading()
                }
                2 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(this)
                }

                else -> {
                    Toaster.showShortToast(
                        this@StartTripActivity,
                        this@StartTripActivity.resources.getString(R.string.somethingwentwrong)
                    )
                    LoadingDialog.hideLoading()
                }
            }
        } else if (response is StartTripResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    if(response.message =="You have already Trip started. Please end trip before start new trip") {
                        ISFROM = "START TRIP"
                        val i = Intent(this@StartTripActivity, MainActivity::class.java)
                        startActivity(i)
                    }
                }
                1 -> {
                    ISFROM = "START TRIP"
                    Toaster.showShortToast(
                        this@StartTripActivity,
                        this.resources.getString(R.string.trip_started_msg_str).plus(" ")
                            .plus(response.data?.tripId)
                    )
                    finish()
                }
                2 -> {
                    response.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(this)
                }
                else -> {
                    Toaster.showShortToast(
                        this@StartTripActivity,
                        this@StartTripActivity.resources.getString(R.string.somethingwentwrong)
                    )
                    LoadingDialog.hideLoading()
                }
            }
        } else if (response is EndTripResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                }
                1 -> {
                    ISFROM = "END TRIP"
                    Toaster.showShortToast(
                        this@StartTripActivity,
                        this.resources.getString(R.string.trip_ended_msg_str)
                    )
                    finish()
                }
                2 -> {
                    response.message?.let {
                        Toaster.showShortToast(
                            this@StartTripActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(this)
                }
                else -> {
                    Toaster.showShortToast(
                        this@StartTripActivity,
                        this@StartTripActivity.resources.getString(R.string.somethingwentwrong)
                    )
                    LoadingDialog.hideLoading()
                }
            }
        }
    }

    override fun onError(errorBody: String) {
        Log.d("error", errorBody)
    }

}