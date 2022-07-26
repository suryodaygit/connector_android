package com.example.logintask.onboarding.signup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.databinding.ActivityMainSignupBinding
import com.example.logintask.dbclass.DatabaseHandler
import com.example.logintask.dbclass.RegistrationModel
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.*
import com.example.logintask.onboarding.login.LoginActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class SignupActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainSignupBinding
    private var compressedImage: File? = null
    private val PIC_CROP = 2
    private var gstRegex = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}"
    var panCardRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    private var entityText = ""
    private var gstNo = ""
    private var name = ""
    private var mobileNumber = ""
    private var email = ""
    private var panCardNo = ""
    private var seletedEntityPosition = 0

    private val entity = arrayOf(
        "Select Entity",
        "Company(Pvt/Public)",
        "Partnership Firm",
        "Proprietor",
        "Individual"
    )
    private lateinit var viewModel: SignUpViewModel
    private var dynamicPram: ArrayList<DynamicParam> = arrayListOf()
    private lateinit var sendResponseData: SendOTPResponseModel
    private var correlationID = ""
    private var otp = ""

    override fun getLayout() = R.layout.activity_main_signup

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
        binding.button.btn.text = "SIGNUP"

        val entityAdapter = ArrayAdapter(this, R.layout.spinner_list, entity)
        binding.spEntity.adapter = entityAdapter
        binding.spEntity.onItemSelectedListener = this
        entityAdapter.notifyDataSetChanged()

        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setOnClickListener() {
        setTagAreaLocation()
        binding.button.btn.setOnClickListener {
            validate(it)
          //  saveRecord()
         /*   val a = "{ \"stringToEncrypt\" : \"vilas\",\"id\": \"45677\"}"
            val encydata =  EncryptionData()
               encydata.encryptData(a)

            val b = "{\"id\":\"45677\",\"stringToDecrypt\":\"UFGWEINcVab6s8RNznIW3Q==\"}"
            val  encydata1 =  EncryptionData()
            val decryptData = encydata1.decryptData(b)
            Log.d("decreptedString ",decryptData.toString())*/
        }

        binding.tvSendOtp.setOnClickListener {
            val mobileNo = "91" + binding.etMobileNo.text.toString()
            if (mobileNo == "" || mobileNo == "91") {
                showSnackbar(it, "Please enter mobile no")
            } else {
                dynamicPram.clear()
                dynamicPram.add(DynamicParam("otp", ""))
                val sendOtpRequest =
                    SendOTPRequestModel(Data("", dynamicPram, "", "1", mobileNo, "OTP454"))
                viewModel.sendOtp(this,sendOtpRequest)
            }
        }

        binding.btnVerify.setOnClickListener {
            if (binding.etPanCard.text.toString() == "") {
                showSnackbar(it, "Please enter pan card no.")
            } else {
                binding.llName.visibility = View.VISIBLE
                binding.tvName.text = "Name"
            }
        }

        binding.btnGstNoVerify.setOnClickListener {
            if (binding.etGstNo.text.toString() == "") {
                showSnackbar(it, "Please enter gst no.")
            }
        }

        binding.ivClickImage.setOnClickListener {
            chooseImageOption(this)
            //ImagePicker.onPickImage(Constant.PICK_IMG,this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setObserver() {
        viewModel.getSendOTPData().observe(this, Observer {
            sendResponseData = it
            correlationID = it.Data.X_Correlation_ID
            sendOTPDialog(this)
            showToast(this, correlationID)
        })
    }

    override fun setViewModel() {
        viewModel = obtainViewModel(SignUpViewModel::class.java)
    }


    private  fun getData(){
        gstNo = binding.etGstNo.text.toString()
        name = binding.etName.text.toString()
        email = binding.etEmail.text.toString()
        mobileNumber = binding.etMobileNo.text.toString()
        panCardNo = binding.etPanCard.text.toString()

        setDataInPreference(this,Constant.USERNAME,name)
    }

    private fun validate(view:View){
        getData()
        val p = Pattern.compile(panCardRegex)
        val panCardMatcher = p.matcher(panCardNo)

        val gstPattern = Pattern.compile(gstRegex)
        val gstMacher = gstPattern.matcher(gstNo)

        if(seletedEntityPosition == 0){
           showSnackbar(view,"Please select entity")
        }else if(seletedEntityPosition == 4 && panCardNo =="" && !panCardMatcher.matches()){
            showSnackbar(view,"Please enter valid pan card no")
        }else if(seletedEntityPosition == 4 && name == ""){
            showSnackbar(view,"Please enter name")
        }else if(seletedEntityPosition < 4 && gstNo == "" && !gstMacher.matches()){
            showSnackbar(view,"Please enter valid GST no")
        }else if(seletedEntityPosition < 4 && name == ""){
            showSnackbar(view,"Please enter company name")
        }else if(mobileNumber == "" || mobileNumber.length < 10){
            showSnackbar(view,"Please enter valid mobile no")
        }else if(email == "" || !checkEmail(email)){
            showSnackbar(view,"Please enter valid mail id")
        }else{
           // startActivity(Intent(this,LoginActivity::class.java))
            SuccessDialog(this," Registered successfully, kindly re-login","Login")
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        entityText = binding.spEntity.selectedItem.toString()
        setDataInPreference(this, Constant.SELECTED_ENTITY,entityText)

        seletedEntityPosition = position

        when (position) {
            4 -> {
                binding.llName.visibility = View.VISIBLE
                binding.tvName.text = "Name"
                binding.rlPanCard.visibility = View.VISIBLE
                binding.rlGstNo.visibility = View.GONE
            }
            0 -> {
                binding.llName.visibility = View.GONE
                binding.rlPanCard.visibility = View.GONE
                binding.rlGstNo.visibility = View.GONE
            }
            else -> {
                binding.llName.visibility = View.VISIBLE
                binding.tvName.text = "Company Name"
                binding.rlGstNo.visibility = View.VISIBLE
                binding.rlPanCard.visibility = View.GONE
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendOTPDialog(mContext: Context) {
        val builder = android.app.AlertDialog.Builder(mContext)
        val view = LayoutInflater.from(mContext).inflate(
            R.layout.custom_logout_dialog, null
        )
        builder.setView(view)
        val btn_yes = view.findViewById<Button>(R.id.btn_yes)
        val btn_no = view.findViewById<Button>(R.id.btn_no)
        val tv_msg = view.findViewById<TextView>(R.id.tv_text)
        val tv_hide = view.findViewById<TextView>(R.id.tv_otp)
        val et_otp = view.findViewById<EditText>(R.id.et_otp)
        tv_hide.visibility = View.GONE
        et_otp.visibility = View.VISIBLE
        tv_msg.setTextColor(getColor(R.color.subTitle))

        btn_yes.text = "Resend Otp"
        btn_no.text = "Verify Otp"
        tv_msg.text = "Enter SMS Code"
        val alertDialog = builder.create()
        alertDialog.show()

        btn_yes.setOnClickListener {
            val mobileNo = "91" + binding.etMobileNo.text.toString()
            if (mobileNo == "" || mobileNo == "91") {
                showSnackbar(it, "Please enter mobile no")
            } else {
                dynamicPram.clear()
                dynamicPram.add(DynamicParam("otp", ""))
                val sendOtpRequest =
                    SendOTPRequestModel(Data("", dynamicPram, "", "1", mobileNo, "OTP454"))
                viewModel.sendOtp(this,sendOtpRequest)
            }
        }

        btn_no.setOnClickListener {
            alertDialog.dismiss()
            viewModel.validateOTP(correlationID,et_otp.text.toString())

        }

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
                               var latitude = location.latitude.toString()
                               var longitude = location.longitude.toString()
                                var geocoder: Geocoder
                                var addresses: List<Address>
                                geocoder = Geocoder(this@SignupActivity, Locale.getDefault())

                                addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(),
                                    1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                val address: String =
                                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                val city: String = addresses[0].getLocality()
                                val state: String = addresses[0].getAdminArea()
                                val country: String = addresses[0].getCountryName()
                                val postalCode: String = addresses[0].getPostalCode()
                                val knownName: String = addresses[0].getFeatureName()
                               val location =
                                    "" + location.latitude.toString() + ", " +
                                            "" + location.longitude.toString()

                                val locationAddress = location +" "+ address
                                Log.d("loctaion",locationAddress)
                                setDataInPreference(this@SignupActivity,Constant.LOCATION,locationAddress)
                            }
                            // Few more things we can do here:
                            // For example: Update the location of user on server
                        }
                    },
                    it
                )
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  openCameraApp(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_GALLERY_IMAGES_CODE) {
                performCropLib(data!!.data!!)
                val path = data.data
                val file = File(path?.path)
                val file_size: Int = java.lang.String.valueOf(file.length() / 1024).toInt()
                Log.i(
                    "Tag",
                    "Image Size before crop = " + file_size
                )
            } else if (requestCode == CAMERA_REQUEST) {
                /*if (cameraFilePath != null && cameraOutURI != null){
                    val file = File(cameraFilePath)
                    val file1 = File(cameraOutURI!!.path)
                    val file1_size: Int = java.lang.String.valueOf(file.length() / 1024).toInt()
                    Log.i(
                        "Tag",
                        "Image Size before crop = " + file1_size
                    )
                    performCropLib(file.path.toUri())
                }*/

                val imageFile = File(Constant.mCurrentPhotoPath!!)
                performCropLib(imageFile.toUri())

               /* val i = imageFile.length()
                if (imageFile.exists()) {
                    customCompressImage(imageFile)
                    val bitmapImage = BitmapFactory.decodeFile(Constant.mCurrentPhotoPath!!)
                    val nh = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                    val scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true)
                    binding.profileImage.setImageBitmap(scaled)

                }*/

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
                        Log.i(
                            "Tag",
                            "onActivityResult: Crop URI = " + resultUri.path
                        )

                        val file = File(resultUri.path)
                        val file_size: Int = java.lang.String.valueOf(file.length() / 1024).toInt()
                        Log.i(
                            "Tag",
                            "Image Size after crop = " + file_size
                        )
                        setDataInPreference(
                            this@SignupActivity,
                            Constant.PROFILE_IMAGE,
                            resultUri.toString()
                        )
                        binding.profileImage.setImageURI(resultUri)
                    }
                }
        }
       /* when (requestCode) {
            Constant.PICK_IMG -> {
                ImagePicker.getImageFromResultUri(this,
                    resultCode, data)?.let {
                    CropImage.activity(it)
                        .start(this)
                }
            }
            Constant.PICK_IMG -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    try {
                        binding.profileImage.setImageURI(result.uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                }
            }
        }*/
    }

    fun customCompressImage(fileName: File) {
        fileName?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(this@SignupActivity, imageFile) {
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

    private fun performCropLib(uri: Uri) {
// start cropping activity for pre-acquired image saved on the device
        Log.i("Tag", "performCropLib: URI = $uri")
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setAspectRatio(1, 1)
            .setMultiTouchEnabled(true)
            .start(this);
   }


  /*RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCameraApp()
                }
            }
        }
    }*/

    //method for saving records in database
    fun saveRecord() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val mobile = binding.etMobileNo.text.toString()
        val pass = binding.etEmail.text.toString()
        val databaseHandler = DatabaseHandler(this)
        if (name.trim() != "" && email.trim() != "" && mobile.trim() != "" && pass.trim() != "") {
            val status = databaseHandler.addCustomer(RegistrationModel(name, email, mobile, pass))
            if (status > -1) {

                showToast(this, "record save")
                binding.etName.text.clear()
                binding.etEmail.text.clear()
                binding.etMobileNo.text.clear()
                binding.etPanCard.text.clear()
            } else {
                showToast(this, "not record save")
            }
        } else {
            showToast(this, "field cannot be blank")
        }

    }

    //method for read records from database in ListView
    fun viewRecord() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val emp: List<RegistrationModel> = databaseHandler.viewEmployee()
        val empArrayName = Array<String>(emp.size) { "null" }
        val empArrayEmail = Array<String>(emp.size) { "null" }
        val empArrayMobile = Array<String>(emp.size) { "null" }
        val empArrayPass = Array<String>(emp.size) { "null" }
        var index = 0
        for (e in emp) {
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            empArrayMobile[index] = e.userMobile
            empArrayPass[index] = e.userPassword
            index++
        }
    }

}