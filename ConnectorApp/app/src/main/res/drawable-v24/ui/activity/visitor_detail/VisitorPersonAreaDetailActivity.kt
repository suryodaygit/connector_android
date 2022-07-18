package com.cmrk.ui.activity.visitor_detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.VisitedPersonAreaDetailBinding
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.ui.activity.startTrip.UploadImageFileResponseModel
import com.cmrk.ui.activity.vehicleBuying.ScrapVehicleBuyingActivity
import com.cmrk.ui.activity.vehicleBuying.SubmitVisitRequestModel
import com.cmrk.util.*
import com.cmrk.util.Controller.Companion.format
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import id.zelory.compressor.Compressor
import id.zelory.compressor.compressFormat
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
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class VisitorPersonAreaDetailActivity : AppCompatActivity(), VisitorPersonAreaDetailNavigator,
    NetworkCallBack, deleteImage {

    companion object {
        const val FROM = "Visitor Person Details Activity"
    }

    private lateinit var activityvisitorAreaDetailBinding: VisitedPersonAreaDetailBinding
    lateinit var selectedVisitor: String
    var bundle: Bundle? = null
    var submitLeadReqModel: SubmitLeadReqModel? = null
    private var visitorPersonAreaDetailViewModel: VisitoPersonAreaDetailViewModel? = null
    var latitude: String = ""
    var longitude: String = ""
    private val MY_CAMERA_PERMISSION_CODE = 100
    private var mCurrentPhotoPath: String? = null
    private val PICK_GALLERY_IMAGES_CODE = 0
    private var images: ArrayList<String> = arrayListOf()
    private var placeImages: ArrayList<String> = arrayListOf()
    private lateinit var imageList: MutableList<Data>
    private lateinit var placeImageList: MutableList<Data>
    private var businessList = arrayListOf<String>()
    private var placeImageNameList = arrayListOf<String>()
    private lateinit var fileParts: Array<MultipartBody.Part?>
    private var submitVisitReqModel: SubmitVisitRequestModel? = null
    private lateinit var imageListAdapter: ImageListAdapter
    private var isFromPlace = false
    private var selectedImagePosition = 0
    private var compressedImage: File? = null

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        images.clear()
        activityvisitorAreaDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.visited_person_area_detail)

        visitorPersonAreaDetailViewModel = this?.let { VisitoPersonAreaDetailViewModel(this, it) }

        activityvisitorAreaDetailBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityvisitorAreaDetailBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityvisitorAreaDetailBinding.toolbar.tvActivityTitle.text =
            this.resources.getString(R.string.visitor_detail_title)

        selectedVisitor = intent.getStringExtra("VisitorPerson").toString()
        activityvisitorAreaDetailBinding.txtDate.text =
            DateUtils.getCurrentDate(Calendar.getInstance().time)
        bundle = this@VisitorPersonAreaDetailActivity.intent.extras
        submitVisitReqModel = bundle?.getSerializable("VisitReqModel") as SubmitVisitRequestModel?
        submitVisitReqModel?.date =
            DateUtils.getCurrentDateWithSimpleDateFormat(Calendar.getInstance().time)

        activityvisitorAreaDetailBinding.lylBusinessCardPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isFromPlace = false
                chooseOption()
            }
        }
        activityvisitorAreaDetailBinding.lylVehiclePhoto.setOnClickListener {
            isFromPlace = true
            chooseOption()
        }
        setTagAreaLocation()

        activityvisitorAreaDetailBinding.btnNext.setOnClickListener(object : View.OnClickListener {
            //            if (latitude == "" && longitude == "") {
            ////                Toaster.shortToast(this, this.resources.getString(R.string.location_validation))
////            }
            override fun onClick(view: View?) {
                if (activityvisitorAreaDetailBinding.edtMobileNumber.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.mobile_number_validation)
                    )
                } else if (activityvisitorAreaDetailBinding.edtGarageName.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.garage_name_validation)
                    )
                } else if (activityvisitorAreaDetailBinding.edtContactPerson.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.contact_person_validation)
                    )
                } else if (activityvisitorAreaDetailBinding.edtOwnerName.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.owner_name_validation)
                    )
                } else if (activityvisitorAreaDetailBinding.edtOwnerMobNo.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.owner_mobile_number_validation)
                    )
                } else if (activityvisitorAreaDetailBinding.edtNumberOfVisit.text.toString() == "") {
                    Toaster.shortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.visit_number_validation)
                    )
                } else {

                    val mobileNo = activityvisitorAreaDetailBinding.edtMobileNumber.text.toString()
                    submitVisitReqModel?.mobile_number = mobileNo
                    val garageName = activityvisitorAreaDetailBinding.edtGarageName.text.toString()
                    submitVisitReqModel?.garage_name = garageName
                    val contactPerson =
                        activityvisitorAreaDetailBinding.edtContactPerson.text.toString()
                    submitVisitReqModel?.contact_person = contactPerson
                    val ownerName = activityvisitorAreaDetailBinding.edtOwnerName.text.toString()
                    submitVisitReqModel?.owner_name = ownerName
                    val ownerMobile = activityvisitorAreaDetailBinding.edtOwnerMobNo.text.toString()
                    submitVisitReqModel?.owner_mobile_number = ownerMobile
                    val noOfVisit =
                        activityvisitorAreaDetailBinding.edtNumberOfVisit.text.toString()
                    submitVisitReqModel?.no_of_visit = noOfVisit
                    val remark = activityvisitorAreaDetailBinding.edtRemark.text.toString()
                    submitVisitReqModel?.remarks = remark

                    submitVisitReqModel?.photo_of_place = arrayListOf()
                    submitVisitReqModel?.business_card = arrayListOf()


                    val aintent = Intent(
                        this@VisitorPersonAreaDetailActivity,
                        ScrapVehicleBuyingActivity::class.java
                    )

                    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                    aintent.putExtra(FROM, "Visit Person Details Activity")
                    aintent.putExtra("MOBILE_NO", mobileNo)
                    aintent.putExtra("GARAGE_NAME", garageName)
                    aintent.putExtra("CONTACT_PERSON", contactPerson)
                    aintent.putExtra("OWNER_NAME", ownerName)
                    aintent.putExtra("OWNER_MOBILE_NO", ownerMobile)
                    aintent.putExtra("NO_OF_VISIT", noOfVisit)
                    aintent.putExtra("REMARK", remark)
                    aintent.putExtra("PLACE_PHOTO", "")
                    aintent.putExtra("BUSINESS_PHOTO", "")
                    aintent.putExtra("LATITUDE", latitude)
                    aintent.putExtra("LONGITUDE", longitude)
                    aintent.putExtra("SELECTED_VISITOR", selectedVisitor)
                    aintent.putExtra("DATE", currentDate)
                    aintent.putExtra("TRIP_ID", intent.getStringExtra("tripId"))
                    aintent.putStringArrayListExtra("BUSINESS", businessList)
                    aintent.putStringArrayListExtra("PLACE", placeImageNameList)
                    startActivity(aintent)
                }
            }

        })

        var garageName = ""
        activityvisitorAreaDetailBinding.edtGarageName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0!!.length > 0 && p0 != "") {
                    garageName = p0.toString()
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            visitorPersonAreaDetailViewModel?.getNoOfVisit(garageName)
                        }
                    }, 2000)
                } else {
                    activityvisitorAreaDetailBinding.edtNumberOfVisit.setText(" ")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        activityvisitorAreaDetailBinding.chkNameOfPersonMet.setOnClickListener {
            if (activityvisitorAreaDetailBinding.chkNameOfPersonMet.isChecked) {
                activityvisitorAreaDetailBinding.edtOwnerName.text =
                    activityvisitorAreaDetailBinding.edtContactPerson.text
            } else {
                activityvisitorAreaDetailBinding.edtOwnerName.setText(" ")
            }
        }
    }

    private fun openCameraApp2() {
        if (!PermissionHelper.checkPermission(
                this!!,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || !PermissionHelper.checkPermission(
                this!!,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            PermissionHelper.askPermission(
                this!!, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                REQUEST_CODE_STORAGE_CAMERA_PERMISSION
            )
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file: File = createFile()

            val uri: Uri = FileProvider.getUriForFile(
                this!!,
                "${this?.packageName}.fileprovider",
                file
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_PHOTO)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openCameraApp() {
        if (!PermissionHelper.checkPermission(
                this!!,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || !PermissionHelper.checkPermission(
                this!!,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            PermissionHelper.askPermission(
                this!!, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                REQUEST_CODE_STORAGE_CAMERA_PERMISSION
            )
        } else {
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
    }

    private fun openGallery() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent()
            val file: File = createFile()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_GALLERY_IMAGES_CODE
            )
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
            );

        }
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

    override fun onResume() {
        super.onResume()
    }

    private fun setTagAreaLocation() {
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
                            latitude = location.latitude.toString()
                            longitude = location.longitude.toString()
                            activityvisitorAreaDetailBinding.txtTagArea.text =
                                "" + location.latitude.toString() + ", " +
                                        "" + location.longitude.toString()
                            submitVisitReqModel?.latitude = location.latitude.toString()
                            submitVisitReqModel?.longitude = location.longitude.toString()
                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                it
            )
        }
    }


    fun customCompressImage(fileName:File) {
        fileName?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(this@VisitorPersonAreaDetailActivity, imageFile) {
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
            val i = imageFile.length()
            if (imageFile.exists()) {
                customCompressImage(imageFile)

                val bitmapImage = BitmapFactory.decodeFile(mCurrentPhotoPath!!)
                val nh = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true)
                activityvisitorAreaDetailBinding.imgShowCapturedPhoto.visibility = View.VISIBLE
                activityvisitorAreaDetailBinding.imgShowCapturedPhoto.setImageBitmap(scaled)


            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_PHOTO && resultCode == RESULT_OK) {
            val imageFile = File(mCurrentPhotoPath!!)
            if (imageFile.exists()) {
               // Controller.OdoMeterFile = imageFile
                customCompressImage(imageFile)
                val bitmapImage = BitmapFactory.decodeFile(mCurrentPhotoPath!!)
                val nh = (bitmapImage.height * (500.0 / bitmapImage.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmapImage, 500, nh, true)


                activityvisitorAreaDetailBinding.imgShowCapturedVehiclePhoto.visibility = View.VISIBLE
                activityvisitorAreaDetailBinding.imgShowCapturedVehiclePhoto.setImageBitmap(scaled)
            }
          //  uploadOdomoterPhoto()
        } else if (requestCode == PICK_GALLERY_IMAGES_CODE && resultCode == Activity.RESULT_OK) {
            // for single image from gallary
            if (data?.clipData == null) {
                val path = data?.data
                path?.let { getPathFromURI(it) }

                val bodyBuilder = MultipartBody.Builder()
                val builder: MultipartBody.Builder = bodyBuilder.setType(MultipartBody.FORM)

                if (isFromPlace) {

                    for (i in 0 until placeImages.size) {
                        Controller.filename = "file[$i]"
                        var imagesEncoimagededList = placeImages[i]
                        var imageFile = File(placeImages[i])
                        val file = File(imagesEncoimagededList)
                        builder.addFormDataPart(
                            Controller.filename, file.name,
                            RequestBody.Companion.create(
                                "application/octet-stream".toMediaTypeOrNull(),
                                imageFile
                            )
                        )

                    }

                } else {
                    for (i in 0 until images.size) {
                        Controller.filename = "file[$i]"
                        var imagesEncoimagededList = images[i]
                        var imageFile = File(images[i])
                        val file = File(imagesEncoimagededList)
                        builder.addFormDataPart(
                            Controller.filename, file.name,
                            RequestBody.Companion.create(
                                "application/octet-stream".toMediaTypeOrNull(),
                                imageFile
                            )
                        )

                    }
                }
                builder.build()

                val documentbodyreq: RequestBody = builder.build()

                visitorPersonAreaDetailViewModel?.getVisitorPersonAreaDetail(documentbodyreq)

            } else if (data?.clipData != null) {
                //for multiple image
                val count = data.clipData!!.itemCount
                if (count > 1) {
                    for (i in 0..count - 1) {
                        val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        getPathFromURI(imageUri)
                    }
                    val bodyBuilder = MultipartBody.Builder()
                    val builder: MultipartBody.Builder = bodyBuilder.setType(MultipartBody.FORM)

                    if (isFromPlace) {

                        for (i in 0 until placeImages.size) {
                            Controller.filename = "file[$i]"
                            var imagesEncoimagededList = placeImages[i]
                            var imageFile = File(placeImages[i])
                            val file = File(imagesEncoimagededList)
                            builder.addFormDataPart(
                                Controller.filename, file.name,
                                RequestBody.Companion.create(
                                    "application/octet-stream".toMediaTypeOrNull(),
                                    imageFile
                                )
                            )

                        }

                    } else {
                        for (i in 0 until images.size) {
                            Controller.filename = "file[$i]"
                            var imagesEncoimagededList = images[i]
                            var imageFile = File(images[i])
                            val file = File(imagesEncoimagededList)
                            builder.addFormDataPart(
                                Controller.filename, file.name,
                                RequestBody.Companion.create(
                                    "application/octet-stream".toMediaTypeOrNull(),
                                    imageFile
                                )
                            )

                        }
                    }
                    builder.build()

                    val documentbodyreq: RequestBody = builder.build()

                    visitorPersonAreaDetailViewModel?.getVisitorPersonAreaDetail(documentbodyreq)

                }
            }

        }
    }

    private fun getPathFromURI(uri: Uri) {
        var path: String = uri.path.toString()

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor?.moveToFirst() == true) {
                val columnIndex = cursor?.getColumnIndex(projection[0])
                val imagePath = columnIndex?.let { cursor.getString(it) }
                // Log.e("path", imagePath);
                if (isFromPlace) {
                    imagePath?.let { placeImages.add(it) }
                } else {
                    imagePath?.let { images.add(it) }
                }
            }
            cursor?.close()
        } catch (e: Exception) {
            Log.e("TAG", e.message, e)
        }
    }

    private fun setBusinessImageAdapter(imageList: List<Data>) {
        if (isFromPlace) {
            activityvisitorAreaDetailBinding.rvPlaceView.visibility = View.VISIBLE
            imageListAdapter = ImageListAdapter(imageList, this)
            activityvisitorAreaDetailBinding.rvPlaceView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            activityvisitorAreaDetailBinding.rvPlaceView.adapter = imageListAdapter
        } else {
            activityvisitorAreaDetailBinding.rvBusinessView.visibility = View.VISIBLE
            imageListAdapter = ImageListAdapter(imageList, this)
            activityvisitorAreaDetailBinding.rvBusinessView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            activityvisitorAreaDetailBinding.rvBusinessView.adapter = imageListAdapter
        }
    }

    private  fun uploadOdomoterPhoto() {

        val requestFile: RequestBody =
            RequestBody.Companion.create(
                "multipart/form-data".toMediaTypeOrNull(),
                Controller.OdoMeterFile
            )
        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", Controller.OdoMeterFile.name, requestFile)
        // add another part within the multipart request
        LoadingDialog.showLoading(this@VisitorPersonAreaDetailActivity)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(this@VisitorPersonAreaDetailActivity, true).apiRequest(
                ApiProvider.provideApi(this@VisitorPersonAreaDetailActivity).addImage(body),
                this@VisitorPersonAreaDetailActivity
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun chooseOption() {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(
            R.layout.custom_choose_option_alert_dialog, null
        )
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
            if (isFromPlace) {
                openCameraApp2()
            } else {
                openCameraApp()
            }
            alertDialog.dismiss()
        }


    }

    override fun ongetVisitorPersonAreaDetailSuccess(response: VisitorPersonAreaDetailResponseModel) {
        Toaster.showLongToast(
            this@VisitorPersonAreaDetailActivity,
            response.message
        )

        if (isFromPlace) {
            placeImageList = response.data as MutableList<Data>

            setBusinessImageAdapter(placeImageList)
            val list = response.data.size
            for (i in 0..list) {
                val name = response.data[i].file
                placeImageNameList.add(name)
            }
            Log.d("place List", placeImageList.toString())
        } else {
            imageList = response.data as MutableList<Data>
            val list = response.data.size
            setBusinessImageAdapter(imageList)

            for (i in 0..list) {
                val name = response.data[i].file
                Log.d("business List", name)
                businessList.add(name)
            }
            Log.d("business List", businessList.toString())
        }
    }

    override fun ongetNoOFVisitSuccess(response: NoOfVisitResponseModel) {
        if (response.status == 0) {
            response?.message?.let {
                Toaster.showLongToast(
                    this@VisitorPersonAreaDetailActivity,
                    it
                )
            }
            LoadingDialog.hideLoading()
        } else {
            response?.message?.let {
                Toaster.showLongToast(
                    this@VisitorPersonAreaDetailActivity,
                    it
                )
                activityvisitorAreaDetailBinding.edtNumberOfVisit.setText(response.data)
            }
            LoadingDialog.hideLoading()
        }
    }

    override fun ongetdeleteImageSuccess(response: DeleteImageResponseModel) {
        if (response.status == 0) {
            response.message?.let {
                Toaster.showLongToast(
                    this@VisitorPersonAreaDetailActivity,
                    it
                )
            }
            LoadingDialog.hideLoading()
        } else {
            response.message?.let {
                Toaster.showLongToast(
                    this@VisitorPersonAreaDetailActivity,
                    it
                )
            }
            LoadingDialog.hideLoading()
            if (isFromPlace) {
                placeImages.removeAt(selectedImagePosition)
                placeImageList.removeAt(selectedImagePosition)
                setBusinessImageAdapter(placeImageList)
            } else {
                images.removeAt(selectedImagePosition)
                imageList.removeAt(selectedImagePosition)
                setBusinessImageAdapter(imageList)
            }
        }
    }

    override fun onSuccess(response: Any?) {
        if (response is UploadImageFileResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showLongToast(
                            this@VisitorPersonAreaDetailActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()

                }
                1 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@VisitorPersonAreaDetailActivity,
                            it
                        )
                    }
                    //uploadedFileName = response.data?.file.toString()
                    LoadingDialog.hideLoading()
                }
                2 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@VisitorPersonAreaDetailActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(this)
                }

                else -> {
                    Toaster.showShortToast(
                        this@VisitorPersonAreaDetailActivity,
                        this@VisitorPersonAreaDetailActivity.resources.getString(R.string.somethingwentwrong)
                    )
                    LoadingDialog.hideLoading()
                }
            }
        }
    }

    override fun onError(error: String) {
        Log.d("Visitor Details", error)
    }

    override fun onBack() {
    }

    override fun deleteSingleImage(position: Int) {
        selectedImagePosition = position

        if (isFromPlace) {
            visitorPersonAreaDetailViewModel?.getDeleteImage(placeImageList[selectedImagePosition].file)
        } else {
            visitorPersonAreaDetailViewModel?.getDeleteImage(imageList[selectedImagePosition].file)
        }
    }

}