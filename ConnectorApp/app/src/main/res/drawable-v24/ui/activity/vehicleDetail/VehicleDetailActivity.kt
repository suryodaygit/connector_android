package com.cmrk.ui.activity.vehicleDetail

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.ActivityVehicleDetailBinding
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.ui.activity.makeModel.ModelListResponseModel
import com.cmrk.ui.activity.makeModel.SelectModelActivity
import com.cmrk.ui.activity.sparepartsSelling.SparePartsSellingActivity
import com.cmrk.ui.activity.startTrip.UploadImageFileResponseModel
import com.cmrk.ui.activity.visitor_detail.*
import com.cmrk.util.*
import com.cmrk.util.Controller.Companion.FUEL_TYPE
import com.cmrk.util.Controller.Companion.SELECTED_FUEL_TYPE
import com.cmrk.util.Controller.Companion.modelList_ResponseModel
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
import java.util.*


class VehicleDetailActivity : AppCompatActivity(), VehicleDetailNavigator,NetworkCallBack,deleteImage {
    private lateinit var activityVehicleDetailBinding: ActivityVehicleDetailBinding
    private var mCurrentPhotoPath: String? = null
    private var vehicleDetailViewModel: VehicleDetailViewModel? = null
    private var radioButton_veh_ownershp: RadioButton? = null
    private var radioButton_veh_condition: RadioButton? = null
    var bundle: Bundle? = null
    var submitLeadReqModel: SubmitLeadReqModel? = null
    private val PICK_GALLERY_IMAGES_CODE = 0
    private var images: ArrayList<String> = arrayListOf()
    private var uploadImage = arrayListOf<String>()
    private lateinit var imageList: MutableList<Data>
    private var vehicleId=""
    private var selectedImagePosition = 0
    private var compressedImage: File? = null

    companion object {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var MakeName: String? = ""
        var modelListResponseModel: ModelListResponseModel.Data? = null
        var isFuelTypeSelected: Boolean = false
        var isTowingCharge: Boolean = false
        var isORCAvailable: String = "no"
        var vehicleImages: ArrayList<String> = ArrayList()
        lateinit var  empId :String
        lateinit var tripId:String
        lateinit var visitId:String
    }
    var orcAvailable =""
    private var expDate=""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVehicleDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_vehicle_detail)

        vehicleDetailViewModel = this?.let { VehicleDetailViewModel(this, it) }

        activityVehicleDetailBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityVehicleDetailBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityVehicleDetailBinding.toolbar.tvActivityTitle.text =
            "Lead"

        empId=  intent.getStringExtra("EMP_ID").toString()
        tripId =  intent.getStringExtra("TRIP_ID").toString()
        visitId = intent.getStringExtra("VISIT_ID").toString()

        Log.d("Value", empId + tripId + visitId)
//        mStringArray = vehicleImages.toArray() as Array<String>
//
//        var stockArr = arrayOfNulls<String>(stockList.size())
//        stockArr = stockList.toArray(stockArr)


//        for (i in mStringArray.indices) {
//            Log.d("string is", mStringArray[i] as String)
//        }

        bundle = intent.extras
        submitLeadReqModel = bundle?.getSerializable("LeadReqModel") as SubmitLeadReqModel?

        activityVehicleDetailBinding.lylVehiclePhotos.setOnClickListener {
          //  openCameraApp()
            chooseImageOption()
        }

        activityVehicleDetailBinding.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (activityVehicleDetailBinding.edtMakeOem.text.toString().equals("")) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.make_validation)
                    )
                } else if (activityVehicleDetailBinding.edtModel.text.toString().equals("")) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.model_validation)
                    )
                } else if (activityVehicleDetailBinding.edtManufactureYear.text.toString()
                        .equals("")
                ) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.manufacture_year_validation)
                    )
                } else if (activityVehicleDetailBinding.edtRegNo.text.toString().equals("")) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.registration_no_validation)
                    )
                } else if (activityVehicleDetailBinding.txtKerbWeight.text.toString().equals("")) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.kerb_weight_validation)
                    )
                } else if (!isFuelTypeSelected) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.fuel_type_validation)
                    )
                } else if (FUEL_TYPE == "true" && activityVehicleDetailBinding.edtTankCapacity.text.toString()
                        .equals("")
                ) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.tank_capacity_validation)
                    )
                } else if (FUEL_TYPE == "true" && activityVehicleDetailBinding.txtExpDate.text.toString()
                        .equals("")
                ) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.exp_date_validation)
                    )
                } else if (isTowingCharge && activityVehicleDetailBinding.edtTowingCharge.text.toString()
                        .equals("")
                ) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.towing_charge_validation)
                    )
                } else if (activityVehicleDetailBinding.edtExpectedAmount.text.toString()
                        .equals("")
                ) {
                    Toaster.shortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.expected_amount_validation)
                    )
                } else {
                    val selectedId: Int =
                        activityVehicleDetailBinding.rgVehicleOwnershp.checkedRadioButtonId
                    // find the radiobutton by returned id
                    radioButton_veh_ownershp = findViewById<View>(selectedId) as RadioButton

                    val selectedVehicleConditionRdbtn: Int =
                        activityVehicleDetailBinding.rgVehicleCondition.checkedRadioButtonId
                    // find the radiobutton by returned id
                    radioButton_veh_condition =
                        findViewById<View>(selectedVehicleConditionRdbtn) as RadioButton

                    val make=  activityVehicleDetailBinding.edtMakeOem.text.toString()
                    submitLeadReqModel?.make =
                        activityVehicleDetailBinding.edtMakeOem.text.toString()
                    val model =  activityVehicleDetailBinding.edtModel.text.toString()
                    submitLeadReqModel?.model =
                        activityVehicleDetailBinding.edtModel.text.toString()
                    val manufactureYear =  activityVehicleDetailBinding.edtManufactureYear.text.toString()
                    submitLeadReqModel?.manufactureYear =
                        activityVehicleDetailBinding.edtManufactureYear.text.toString()
                    val regNo = activityVehicleDetailBinding.edtRegNo.text.toString()
                    submitLeadReqModel?.registrationNumber =
                        activityVehicleDetailBinding.edtRegNo.text.toString()
                    val kerbWeight = activityVehicleDetailBinding.txtKerbWeight.text.toString()
                    submitLeadReqModel?.kerbWeight =
                        activityVehicleDetailBinding.txtKerbWeight.text.toString()
                   val vehicleOwnerShip =  radioButton_veh_ownershp?.text.toString()
                    submitLeadReqModel?.vehicleOwnrship = radioButton_veh_ownershp?.text.toString()
                    val vehicleCondition = radioButton_veh_condition?.text.toString()
                    submitLeadReqModel?.vehicleCondition =
                        radioButton_veh_condition?.text.toString()
                    val selectedFualType=  SELECTED_FUEL_TYPE
                    submitLeadReqModel?.fuelType = SELECTED_FUEL_TYPE
                    val tankCapacity = activityVehicleDetailBinding.edtTankCapacity.text.toString()
                    submitLeadReqModel?.tankCapacity =
                        activityVehicleDetailBinding.edtTankCapacity.text.toString()
                    submitLeadReqModel?.tankExpiryDate =
                        activityVehicleDetailBinding.txtExpDate.text.toString()
                    var towingCharge =""
                    if (isTowingCharge) {
                        submitLeadReqModel?.towingCharge =
                            activityVehicleDetailBinding.txtExpDate.text.toString()
                        towingCharge = activityVehicleDetailBinding.txtExpDate.text.toString()
                    } else {
                        submitLeadReqModel?.towingCharge =
                            "no"
                        towingCharge = "no"
                    }
                    val expectedAmount = activityVehicleDetailBinding.edtExpectedAmount.text.toString()
                    submitLeadReqModel?.expectedAmount =
                        activityVehicleDetailBinding.edtExpectedAmount.text.toString()

                    submitLeadReqModel?.orcAvailable = isORCAvailable
                    submitLeadReqModel?.vehicleImages = uploadImage

                    if(activityVehicleDetailBinding.switchRcAvailable.isChecked){
                        orcAvailable ="yes"
                    }else{
                        orcAvailable="no"
                    }


                    val intent = Intent(this@VehicleDetailActivity, SparePartsSellingActivity::class.java)
                    intent.putExtra("LeadReqModel", submitLeadReqModel)
                    intent.putExtra("MAKE",make)
                    intent.putExtra("MODEL",model)
                    intent.putExtra("MANUFACTURE_YEAR",manufactureYear)
                    intent.putExtra("REGNO",regNo)
                    intent.putExtra("KERB_WEIGHT",kerbWeight)
                    intent.putExtra("VEHICLE_OWNERSHIP",vehicleOwnerShip)
                    intent.putExtra("VEHICLE_CONDITION",vehicleCondition)
                    intent.putExtra("SELECT_FUEL_TYPE",selectedFualType)
                    intent.putExtra("TANK_CAPACITY",tankCapacity)
                    intent.putExtra("EXP_DATE",expDate)
                    intent.putExtra("TOWING_CHARG",towingCharge)
                    intent.putExtra("EXPECTED_AMOUNT",expectedAmount)
                    intent.putExtra("ORC_AVAILABLE",orcAvailable)
                    intent.putExtra("EMP_ID",empId)
                    intent.putExtra("TRIP_ID",tripId)
                    intent.putExtra("VEHICLE_ID",vehicleId)
                    intent.putExtra("VISIT_ID", visitId)
                    intent.putStringArrayListExtra("VEHICAL_IMAGE",uploadImage)
                    startActivity(intent)
                }

            }

        })

        activityVehicleDetailBinding.edtMakeOem.setOnClickListener {
            val intent = Intent(this, SelectModelActivity::class.java)
            intent.putExtra("FROM", "MAKE")
            intent.putExtra("LeadReqModel", submitLeadReqModel)
            intent.putExtra("VISIT_ID", visitId)
            intent.putExtra("EMP_ID", empId)
            intent.putExtra("TRIP_ID", tripId)
            startActivity(intent)
//            startActivityForResult(intent, REQUEST_CODE_FOR_MAKE_MODEL)
        }

        activityVehicleDetailBinding.edtModel.setOnClickListener {
            val intent = Intent(this, SelectModelActivity::class.java)
            intent.putExtra("LeadReqModel", submitLeadReqModel)
            intent.putExtra("VISIT_ID", visitId)
            intent.putExtra("EMP_ID", empId)
            intent.putExtra("TRIP_ID", tripId)
            intent.putExtra("FROM", "")
            startActivity(intent)
        }

        activityVehicleDetailBinding.lylCng.setOnClickListener {
            SELECTED_FUEL_TYPE = "CNG"
            FUEL_TYPE = "true"
            isFuelTypeSelected = true
            enableSelectedLayout(activityVehicleDetailBinding.lylCng, true, true)
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
        }
        activityVehicleDetailBinding.lylLpg.setOnClickListener {
            SELECTED_FUEL_TYPE = "LPG"
            FUEL_TYPE = "true"
            isFuelTypeSelected = true
            enableSelectedLayout(
                activityVehicleDetailBinding.lylLpg,
                true, true
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
        }
        activityVehicleDetailBinding.lylPetrol.setOnClickListener {
            SELECTED_FUEL_TYPE = "Petrol"
            FUEL_TYPE = ""
            isFuelTypeSelected = true
            enableSelectedLayout(
                activityVehicleDetailBinding.lylPetrol,
                false, false
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
        }
        activityVehicleDetailBinding.lylDiesel.setOnClickListener {
            SELECTED_FUEL_TYPE = "Diesel"
            FUEL_TYPE = ""
            isFuelTypeSelected = true
            enableSelectedLayout(
                activityVehicleDetailBinding.lylDiesel,
                false, false
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
        }
        activityVehicleDetailBinding.lylOthers.setOnClickListener {
            SELECTED_FUEL_TYPE = "Others"
            FUEL_TYPE = ""
            isFuelTypeSelected = true
            enableSelectedLayout(
                activityVehicleDetailBinding.lylOthers,
                false, false
            )
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
        }

        if (FUEL_TYPE == "true") {
            activityVehicleDetailBinding.edtTankCapacity.isEnabled = true
        }

        activityVehicleDetailBinding.txtExpDate.setOnClickListener {
            if (FUEL_TYPE == "true") {
                activityVehicleDetailBinding.txtExpDate.isEnabled = true
                openDatepicker()
            } else {
                activityVehicleDetailBinding.txtExpDate.isEnabled = false
            }

        }

        activityVehicleDetailBinding.swtchTowingCharge.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                activityVehicleDetailBinding.edtTowingCharge.isEnabled = true
                isTowingCharge
            } else {
                !isTowingCharge
                activityVehicleDetailBinding.edtTowingCharge.isEnabled = false
            }
        }
        activityVehicleDetailBinding.switchRcAvailable.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                isORCAvailable = "yes"
                orcAvailable ="yes"
                activityVehicleDetailBinding.imgClickPhoto.visibility = View.VISIBLE
            } else {
                isORCAvailable = "no"
                var orcAvailable ="no"
                activityVehicleDetailBinding.imgClickPhoto.visibility = View.GONE
                activityVehicleDetailBinding.imgRcImage.visibility = View.GONE
            }
        }

        activityVehicleDetailBinding.imgClickPhoto.setOnClickListener {
            openCameraApp2()
//            if (!PermissionHelper.checkPermission(
//                    this@VehicleDetailActivity!!,
//                    Manifest.permission.CAMERA
//                ) || !PermissionHelper.checkPermission(
//                    this@VehicleDetailActivity!!,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            ) {
//                PermissionHelper.askPermission(
//                    this@VehicleDetailActivity!!, arrayOf(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA
//                    ),
//                    REQUEST_CODE_STORAGE_CAMERA_PERMISSION
//                )
//            } else {
//                openCameraApp()
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Controller.MAKE_NAME.equals("")) {
            activityVehicleDetailBinding.edtMakeOem.setText(Controller.MAKE_NAME)
        }
        if (modelList_ResponseModel != null) {
            activityVehicleDetailBinding.edtModel.setText(modelList_ResponseModel?.model)
            modelList_ResponseModel?.id?.let { vehicleDetailViewModel?.getVehicleDetail(it) }
        }
        empId=  intent.getStringExtra("EMP_ID").toString()
        tripId =  intent.getStringExtra("TRIP_ID").toString()
        visitId = intent.getStringExtra("VISIT_ID").toString()
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
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_RC_PHOTO)
        }
    }

    private fun openCameraApp11() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this!!,
            "${this?.packageName}.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_DETAIL_PHOTO)

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
                openCameraApp11()
            alertDialog.dismiss()
        }



    }

    private fun openGallery() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_GALLERY_IMAGES_CODE
            )
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0);

        }
    }

    fun customCompressImage(fileName:File) {
        fileName?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(this@VehicleDetailActivity, imageFile) {
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
        if (requestCode == REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_DETAIL_PHOTO && resultCode == RESULT_OK) {
            val imageFile = File(mCurrentPhotoPath!!)
            if (imageFile.exists()) {
                Controller.OdoMeterFile = imageFile
                val bitmapImage = BitmapFactory.decodeFile(  mCurrentPhotoPath!!)
                val nh = (bitmapImage.height * (500.0 / bitmapImage.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmapImage, 500, nh, true)

                activityVehicleDetailBinding.imgShowCapturedPhoto.visibility = View.VISIBLE
                activityVehicleDetailBinding.imgShowCapturedPhoto.setImageBitmap(scaled)
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_FOR_VEHICLE_RC_PHOTO && resultCode == RESULT_OK) {
            val imageFile = File(mCurrentPhotoPath!!)
            if (imageFile.exists()) {
                // Controller.OdoMeterFile = imageFile
                customCompressImage(imageFile)
                val myBitmapPath =
                    BitmapUtils.getCompressedImage(
                        this,
                        mCurrentPhotoPath!!,
                        ImageView.ScaleType.FIT_CENTER
                    )
                Controller.saveBitmap(
                    BitmapFactory.decodeFile(
                        myBitmapPath
                    ), this
                )
                activityVehicleDetailBinding.imgShowRcPhoto.visibility = View.VISIBLE
                activityVehicleDetailBinding.imgShowRcPhoto.setImageBitmap(
                    BitmapFactory.decodeFile(
                        myBitmapPath
                    )
                )
            }
        } else if (requestCode == REQUEST_CODE_FOR_MAKE_MODEL && resultCode == RESULT_OK) {
            MakeName = data?.getStringExtra("make")
            var bundle = data?.getBundleExtra("model")
            modelListResponseModel = bundle as ModelListResponseModel.Data?
            Toaster.showLongToast(this, MakeName.toString())
            Toaster.showLongToast(this, modelListResponseModel?.model.toString())
        } else if (requestCode == PICK_GALLERY_IMAGES_CODE && resultCode == Activity.RESULT_OK) {

            if(data?.clipData==null){
                val path = data?.data
                path?.let { getPathFromURI(it) }

                val bodyBuilder = MultipartBody.Builder()
                val builder: MultipartBody.Builder = bodyBuilder.setType(MultipartBody.FORM)

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
                builder.build()
                val documentbodyreq: RequestBody = builder.build()
                vehicleDetailViewModel?.getVisitorPersonAreaDetail(documentbodyreq)

            }else if(data?.clipData !=null) {
                val count = data.clipData!!.itemCount
                if(count>1){
                    for(i in  0..count-1){
                        val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        getPathFromURI(imageUri)
                    }
                    val bodyBuilder = MultipartBody.Builder()
                    val builder: MultipartBody.Builder = bodyBuilder.setType(MultipartBody.FORM)

                    for (i in 0 until images.size) {
                        Controller.filename = "file[$i]"
                        var  imagesEncoimagededList = images[i]
                        var imageFile = File(images[i])
                        val  file = File(imagesEncoimagededList)
                        builder.addFormDataPart(
                            Controller.filename, file.name,
                            RequestBody.Companion.create(
                                "application/octet-stream".toMediaTypeOrNull(),
                                imageFile
                            )
                        )

                    }
                    builder.build()

                    val documentbodyreq: RequestBody = builder.build()

                    vehicleDetailViewModel?.getVisitorPersonAreaDetail(documentbodyreq)

                }
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
        LoadingDialog.showLoading(this@VehicleDetailActivity)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(this@VehicleDetailActivity, true).apiRequest(
                ApiProvider.provideApi(this@VehicleDetailActivity).addImage(body),
                this@VehicleDetailActivity
            )
        }
    }


    private fun getPathFromURI(uri: Uri) {
        var path: String = uri.path.toString() // uri = any content Uri

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
                imagePath?.let { images.add(it) }
            }
            cursor?.close()
        } catch (e: Exception) {
            Log.e("TAG", e.message, e)
        }
    }

    private fun enableSelectedLayout(
        layout: LinearLayout, isTankCapacity: Boolean,
        isClickableExpiryDate: Boolean
    ) {
        layout.background =
            resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        activityVehicleDetailBinding.edtTankCapacity.isEnabled = isTankCapacity
        activityVehicleDetailBinding.txtExpDate.isEnabled = isClickableExpiryDate
    }

    fun openDatepicker() {
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val selectedMonth = month + 1
                // Display Selected date in TextView
                activityVehicleDetailBinding.txtExpDate.text =
                    "$dayOfMonth/$selectedMonth/$year"
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("HH:mm:ss")
                val currentTime = mdformat.format(calendar.getTime())
                expDate = "$dayOfMonth-$selectedMonth-$year $currentTime"

            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    override fun ongetVehicleDetailSuccess(response: VehicleDetailResponseModel) {
        LoadingDialog.hideLoading()
        submitLeadReqModel?.vehicleId = response.data?.id
        vehicleId = response.data?.id.toString()
        activityVehicleDetailBinding.txtKerbWeight.text = response.data?.kerb
        if (response.data?.fuelType.equals("Petrol")) {
            isFuelTypeSelected = true
            FUEL_TYPE = ""
            SELECTED_FUEL_TYPE = "Petrol"
            enableSelectedLayout(
                activityVehicleDetailBinding.lylPetrol,
                false, false
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)

//            activityVehicleDetailBinding.lylPetrol.background =
//                resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        } else if (response.data?.fuelType.equals("Diesel")) {
            isFuelTypeSelected = true
            FUEL_TYPE = ""
            SELECTED_FUEL_TYPE = "Diesel"
            enableSelectedLayout(
                activityVehicleDetailBinding.lylDiesel,
                false, false
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)

//            activityVehicleDetailBinding.lylDiesel.background =
//                resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        } else if (response.data?.fuelType.equals("CNG")) {
            isFuelTypeSelected = true
            FUEL_TYPE = "true"
            SELECTED_FUEL_TYPE = "CNG"
            enableSelectedLayout(activityVehicleDetailBinding.lylCng, true, true)
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)

//            activityVehicleDetailBinding.lylCng.background =
//                resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        } else if (response.data?.fuelType.equals("LPG")) {
            isFuelTypeSelected = true
            FUEL_TYPE = "true"
            SELECTED_FUEL_TYPE = "LPG"
            enableSelectedLayout(
                activityVehicleDetailBinding.lylLpg,
                true, true
            )
            activityVehicleDetailBinding.lylOthers.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)

//            activityVehicleDetailBinding.lylLpg.background =
//                resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        } else if (response.data?.fuelType.equals("Others")) {
            isFuelTypeSelected = true
            FUEL_TYPE = ""
            SELECTED_FUEL_TYPE = "Others"
            enableSelectedLayout(
                activityVehicleDetailBinding.lylOthers,
                false, false
            )
            activityVehicleDetailBinding.lylDiesel.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylPetrol.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylLpg.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
            activityVehicleDetailBinding.lylCng.background =
                resources.getDrawable(R.drawable.fuel_type_border_bg)
//            activityVehicleDetailBinding.lylOthers.background =
//                resources.getDrawable(R.drawable.fuel_type_bordere_enable_bg)
        }

        modelList_ResponseModel = null
    }

    private fun setBusinessImageAdapter(imageList: MutableList<Data>) {
            activityVehicleDetailBinding.rvPlaceView.visibility = View.VISIBLE
            val imageListAdapter = ImageListAdapter(imageList,this)
            activityVehicleDetailBinding.rvPlaceView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            activityVehicleDetailBinding.rvPlaceView.adapter = imageListAdapter

    }

    override fun ongetVisitorPersonAreaDetailSuccess(response: VisitorPersonAreaDetailResponseModel) {
        Toaster.showLongToast(
            this,
            response.message)
        imageList = response.data as MutableList<Data>
        setBusinessImageAdapter(imageList)
        for(i in 0..response.data.size) {
            val name = response.data[i].file
             uploadImage.add(name)
        }

    }

    override fun ongetImageDeleteSuccess(response: DeleteImageResponseModel) {
        if (response.status==0){
            response.message?.let {
                Toaster.showLongToast(
                    this@VehicleDetailActivity,
                    it
                )
            }
            LoadingDialog.hideLoading()
        }else{
            response.message?.let {
                Toaster.showLongToast(
                    this@VehicleDetailActivity,
                    it
                )
            }
            LoadingDialog.hideLoading()
            images.removeAt(selectedImagePosition)
            imageList.removeAt(selectedImagePosition)
            setBusinessImageAdapter(imageList)
        }
    }

    override fun onSuccess(response: Any?) {
        if (response is UploadImageFileResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showLongToast(
                            this@VehicleDetailActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()

                }
                1 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@VehicleDetailActivity,
                            it
                        )
                    }
                    //uploadedFileName = response.data?.file.toString()
                    LoadingDialog.hideLoading()
                }
                2 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            this@VehicleDetailActivity,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(this)
                }

                else -> {
                    Toaster.showShortToast(
                        this@VehicleDetailActivity,
                        this@VehicleDetailActivity.resources.getString(R.string.somethingwentwrong)
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
        vehicleDetailViewModel?.getDeleteImage(imageList[selectedImagePosition].file)

    }


}