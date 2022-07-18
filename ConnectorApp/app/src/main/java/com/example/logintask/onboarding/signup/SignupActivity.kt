package com.example.logintask.onboarding.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.databinding.ActivityMainSignupBinding
import com.example.logintask.dbclass.DatabaseHandler
import com.example.logintask.dbclass.RegistrationModel
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.*
import com.example.logintask.onboarding.login.LoginActivity
import com.suryodaybank.suryodaylib.common.EncryptionData
import java.util.regex.Pattern

class SignupActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainSignupBinding
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

    private fun setOnClickListener() {
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
                binding.tvName.text = "Enter Name"
            }
        }

        binding.btnGstNoVerify.setOnClickListener {
            if (binding.etGstNo.text.toString() == "") {
                showSnackbar(it, "Please enter gst no.")
            }
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
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        entityText = binding.spEntity.selectedItem.toString()
        setDataInPreference(this, Constant.SELECTED_ENTITY,entityText)

        seletedEntityPosition = position

        when (position) {
            4 -> {
                binding.llName.visibility = View.VISIBLE
                binding.tvName.text = "Enter Name"
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