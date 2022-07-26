package com.example.logintask.dashboard.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
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
    private val name = arrayOf("Mr","Mrs","M/S","Ms")
    private val occupication = arrayOf ("Real Estate Consultant - Residential","Real Estate Consultant - Commercial",
        "Real Estate Consultant - Residential & Consultant","Real Estate Developer","Chartered Accountant",
        "Tax Consultant","Personal Finance Advisor","Pvt Ltd Company","Public Ltd Company","Cooperative Bank",
        "Cooperative Housing Society","Insurance Advisor - ICICI Prudential","Insurance Advisor - Others","Others")
    private var aadharMobileCheck=""
    private lateinit var selectedRadioButton: RadioButton
    // Regex to check valid PAN Card number.
    var panCardRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    var aadharRegex = "^[0-9]{4}[ -]?[0-9]{4}[ -]?[0-9]{4}\$"
    // Compile the ReGex
    private var companyName=""
    private var panCardNo=""
    private var nameText=""
    private var mobileNo=""
    private var alternateMobileNo = ""
    private var email=""
    private var aadharCardNo=""
    private var dob=""
    private var selectedEntity = ""
    private var presentedOccupication = ""
    private var gstNo=""


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
        selectedEntity =getPreferenceData(this,Constant.SELECTED_ENTITY,"")
        binding.etEntity.setText(selectedEntity)


        if(selectedEntity == "Individual"){
            binding.llGstNo.visibility = View.GONE
        }else{
            binding.llGstNo.visibility = View.VISIBLE
        }

        if(binding.etEntity.text.toString() !=""){
            binding.etEntity.isFocusable = false
            binding.etEntity.isClickable = false
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
            finish()
        }

        binding.rgAadharMobileLink.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId =radioGroup.checkedRadioButtonId
            if(selectedbuttonId !=-1){
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                aadharMobileCheck = selectedRadioButton.text.toString()
            }
/*            if(aadharMobileCheck=="Yes"){
                binding.llAadharMobileLink.visibility = View.VISIBLE
            }else{
                binding.llAadharMobileLink.visibility = View.GONE
            }*/
        })

        binding.btnSubmit.btn.setOnClickListener{
            validation(it)
        }

        binding.btnVerify.setOnClickListener {
            aadharCardNo= binding.etAadharCardNo.text.toString()
            val lastfourno = aadharCardNo.takeLast(4)
            binding.etAadharCardNo.setText("xxxxxxxx$lastfourno")

        }
    }

    private fun setSpinner(){
        val occupicationAdapter = ArrayAdapter(this, R.layout.spinner_list, occupication)
        binding.spPresentOccupation.adapter = occupicationAdapter
        binding.spPresentOccupation.onItemSelectedListener = this
        occupicationAdapter.notifyDataSetChanged()

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
        presentedOccupication = binding.spPresentOccupation.selectedItem.toString()
        if(presentedOccupication == "Others"){
            binding.llOtherOccupation.visibility = View.VISIBLE
        }else{
            binding.llOtherOccupation.visibility = View.GONE
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun getData(){
        gstNo = binding.etGstNo.text.toString()
        companyName = binding.etCompanyName.text.toString()
        panCardNo = binding.etPanCardNumber.text.toString()
        nameText = binding.etName.text.toString()
        mobileNo = binding.etMobileNumber.text.toString()
        alternateMobileNo = binding.etAlternetMobileNumber.text.toString()
        dob = binding.tvDob.text.toString()
        email = binding.etEmail.text.toString()
        aadharCardNo= binding.etAadharCardNo.text.toString()

        // save data in preference
        setDataInPreference(this,Constant.ENTITY,selectedEntity)
        setDataInPreference(this,Constant.COMPANY_NAME,companyName)
        setDataInPreference(this,Constant.PAN_CARD_NO,panCardNo)
        setDataInPreference(this,Constant.NAME,nameText)
        setDataInPreference(this,Constant.MOBILE_NO,mobileNo)
        setDataInPreference(this,Constant.ALTERNATE_MOBILE_NO,alternateMobileNo)
        setDataInPreference(this,Constant.DOB,dob)
        setDataInPreference(this,Constant.EMAIL,email)
        setDataInPreference(this,Constant.AADHAR_CARD_NO,aadharCardNo)
        setDataInPreference(this, Constant.PRESENT_OCCUPATION,presentedOccupication)
        setDataInPreference(this,Constant.GST_NO,gstNo)
    }

    private fun validation(view:View){
        getData()
        val p = Pattern.compile(panCardRegex)
        val  m = p.matcher(panCardNo)

        val aadharPattern = Pattern.compile(aadharRegex)
        val aadharMatcher = aadharPattern.matcher(aadharCardNo)

        if(selectedEntity !="Individual" && companyName==""){
            showSnackbar(view,"Please enter company name")
        }else if(panCardNo =="" || !m.matches()){
            showSnackbar(view,"Please enter valid  pan card no")
        }else if(nameText==""){
            showSnackbar(view,"Please enter name")
        }else if(mobileNo=="" || mobileNo.length < 10){
            showSnackbar(view,"Please enter mobile no")
        }else if (aadharCardNo == ""){
            showSnackbar(view,"Please enter valid aadhar no")
        } else if(email =="" || !checkEmail(email)){
            showSnackbar(view,"Please enter valid email id")
        } else if(dob ==""){
            showSnackbar(view,"Please enter dob")
        }else if(alternateMobileNo !="" && alternateMobileNo == mobileNo){
            showSnackbar(view,"Alternate mobile no should be different than mobile no.")
        }else if(presentedOccupication == "Others" && binding.etOccupation.text.toString()== "") {
            showSnackbar(view, "Please enter other occupation")
        }else if(presentedOccupication ==""){
            showSnackbar(view,"Please select occupation")
        }else if(gstNo==""){
            showSnackbar(view,"Please enter gst no")
        }else{
            HomeFragment.COMPLETED_PAGE_STATUS ="Basic"
            finish()
        }
    }

}