package com.example.logintask.dashboard.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityBasicBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.checkEmail
import com.example.logintask.lib.utils.getPreferenceData
import com.example.logintask.lib.utils.setDataInPreference
import com.example.logintask.lib.utils.showSnackbar
import java.util.*
import java.util.regex.Pattern


class BasicActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding : ActivityBasicBinding
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val yearshow = year - 18
    private val name = arrayOf("Mr","Ms","Dr","Mrs","Col","Lt.Col","Maj","Brig","Capt")
    private var msme=""
    private var bsm=""
    private lateinit var selectedRadioButton: RadioButton
    // Regex to check valid PAN Card number.
    var panCardRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    var aadharRegex = "^[0-9]{4}[ -]?[0-9]{4}[ -]?[0-9]{4}\$"
    // Compile the ReGex
    private var entity=""
    private var companyName=""
    private var panCardNo=""
    private var nameText=""
    private var mobileNo=""
    private var alternateMobileNo = ""
    private var email=""
    private var aadharCardNo=""
    private var gstNo=""
    private var msmeRegistrationNo=""
    private var bsmId=""
    private var dob=""

    override fun getLayout() = R.layout.activity_basic

    override fun init() {
        overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.toolbar.tvActivityTitle.text = "Basic Details"
        binding.btnSubmit.btn.text = "Submit"
        c.add(Calendar.YEAR,-18)
        setOnclickListener()
        setSpinner()
    }

    private fun setOnclickListener(){
        val selectedEntity = getPreferenceData(this,Constant.SELECTED_ENTITY,"")
        if(selectedEntity != "Select Entity") {
            binding.etEntity.setText(selectedEntity)
        }else{
            binding.etEmail.setText("")
        }

        if(binding.etEmail.text.toString() == ""){
            binding.etEmail.isFocusable = true
            binding.etEmail.isClickable = true
        }else{
            binding.etEmail.isFocusable = false
            binding.etEmail.isClickable = false
        }
        binding.tvDob.setOnClickListener {
          openDatepicker()
        }
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java))
        }

        binding.rgMsme.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId =radioGroup.checkedRadioButtonId

            if(selectedbuttonId !=-1){
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                msme = selectedRadioButton.text.toString()
                setDataInPreference(this,Constant.MSME_CHECK,msme)
            }
            if(msme=="Yes"){
                binding.llMsmeNo.visibility = View.VISIBLE
            }else{
                binding.llMsmeNo.visibility = View.GONE
            }
        })


        binding.rgBsm.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId =radioGroup.checkedRadioButtonId
            if(selectedbuttonId !=-1){
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                bsm = selectedRadioButton.text.toString()
                setDataInPreference(this,Constant.BSM_CHECK,bsm)
            }
            if(bsm=="Yes"){
                binding.llBsmNo.visibility = View.VISIBLE
            }else{
                binding.llBsmNo.visibility = View.GONE
            }
        })

        binding.btnSubmit.btn.setOnClickListener{
            validation(it)
        }
    }

    private fun setSpinner(){
        val nameAdapter = ArrayAdapter(this,R.layout.spinner_list, name)
        binding.spName.adapter = nameAdapter
        binding.spName.onItemSelectedListener = this
        nameAdapter.notifyDataSetChanged()
    }
    override fun setObserver() {

    }

    override fun setViewModel() {
    }

    @SuppressLint("SetTextI18n")
    fun openDatepicker() {

        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, yearShow, monthOfYear, dayOfMonth ->
                val selectedMonth = monthOfYear + 1
                // Display Selected date in TextView
                binding.tvDob.setText("$dayOfMonth/$selectedMonth/$yearShow")

            },
            yearshow,
            month,
            day
        )

     //   dpd.datePicker.minDate =c.getTimeInMillis()
        dpd.datePicker.maxDate = c.getTimeInMillis()
        dpd.show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    private fun getData(){
        entity = binding.etEntity.text.toString()
        companyName = binding.etCompanyName.text.toString()
        panCardNo = binding.etPanCardNumber.text.toString()
        nameText = binding.etName.text.toString()
        mobileNo = binding.etMobileNumber.text.toString()
        alternateMobileNo = binding.etAlternetMobileNumber.text.toString()
        gstNo = binding.etGstNo.text.toString()
        dob = binding.tvDob.text.toString()
        email = binding.etEmail.text.toString()
        aadharCardNo = binding.etAadharCardNo.text.toString()
        msmeRegistrationNo = binding.etMsmeNumber.text.toString()
        bsmId = binding.etBsmNumber.text.toString()

        // save data in preference
        setDataInPreference(this,Constant.ENTITY,entity)
        setDataInPreference(this,Constant.COMPANY_NAME,companyName)
        setDataInPreference(this,Constant.PAN_CARD_NO,panCardNo)
        setDataInPreference(this,Constant.NAME,nameText)
        setDataInPreference(this,Constant.MOBILE_NO,mobileNo)
        setDataInPreference(this,Constant.ALTERNATE_MOBILE_NO,alternateMobileNo)
        setDataInPreference(this,Constant.GST_NO,gstNo)
        setDataInPreference(this,Constant.DOB,dob)
        setDataInPreference(this,Constant.EMAIL,email)
        setDataInPreference(this,Constant.AADHAR_CARD_NO,aadharCardNo)
        setDataInPreference(this,Constant.MSME_REGISTRATION_NO,msmeRegistrationNo)
        setDataInPreference(this,Constant.BSM_ID,bsmId)

    }

    private fun validation(view:View){
        getData()
        val p = Pattern.compile(panCardRegex)
        val  m = p.matcher(panCardNo)

        val aadharPattern = Pattern.compile(aadharRegex)
        val aadharMatcher = aadharPattern.matcher(aadharCardNo)

        if(entity==""){
            showSnackbar(view,"Please enter entity")
        }else if(companyName==""){
            showSnackbar(view,"Please enter company name")
        }else if(panCardNo =="" || !m.matches()){
            showSnackbar(view,"Please enter valid  pan card no")
        }else if(nameText==""){
            showSnackbar(view,"Please enter name")
        }else if(mobileNo=="" || mobileNo.length < 10){
            showSnackbar(view,"Please enter mobile no")
        }else if(gstNo==""){
            showSnackbar(view,"Please enter gst no")
        }else if (aadharCardNo == "" || !aadharMatcher.matches()){
            showSnackbar(view,"Please enter valid aadhar no")
        } else if(email =="" || !checkEmail(email)){
            showSnackbar(view,"Please enter valid email id")
        } else if(dob ==""){
            showSnackbar(view,"Please enter dob")
        }else if(msme=="Yes" && msmeRegistrationNo ==""){
            showSnackbar(view,"Please enter msme registration no")
        }else if(bsm=="Yes" && bsmId == ""){
            showSnackbar(view,"Please enter bsm registration Id")
        }else{
            HomeFragment.COMPLETED_PAGE_STATUS ="Basic"
            finish()
        }
    }

}