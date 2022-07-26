package com.example.logintask.dashboard.activity

import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityAddressBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.setDataInPreference
import com.example.logintask.lib.utils.showSnackbar

class AddressActivity : BaseActivity() {

    private lateinit var binding:ActivityAddressBinding
    private var residentialPinCode=""
    private var residentialCity=""
    private var residentialState=""
    private var residentialDistrict = ""
    private var residentialAddress=""
    private var officePinCode=""
    private var officeCity=""
    private var officeState=""
    private var officeAddress=""
    private var officeDistrict = ""

    override fun getLayout() = R.layout.activity_address

    override fun init() {
        binding= DataBindingUtil.setContentView(this,getLayout())
        binding.toolbar.tvActivityTitle.text = "Address Details"
        binding.btnSubmit.btn.text = "Submit"
        setOnCliCk()
    }

    private fun setOnCliCk(){
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.btnSubmit.btn.setOnClickListener {
            validation(it)
        }

      /*  binding.etPincode.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.llResidentialView.visibility = View.VISIBLE

            }
        })

        binding.etOfficePincode.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.llOfficeView.visibility = View.VISIBLE
            }
        })*/
        var isCheck = false
        binding.cbAddress.setOnClickListener {
             isCheck = binding.cbAddress.isChecked
            if (isCheck) {
                residentialPinCode = binding.etPincode.text.toString()
                residentialCity = binding.etCity.text.toString()
                residentialState = binding.etState.text.toString()
                residentialAddress = binding.etAddress.text.toString()
                officePinCode = binding.etOfficePincode.text.toString()
                officeCity = binding.etOfficeCity.text.toString()
                officeState = binding.etOfficeState.text.toString()
                officeAddress = binding.etOfficeAdress.text.toString()
                residentialDistrict = binding.etDistrict.text.toString()
                officeDistrict = binding.etOfficeDistrict.text.toString()

                binding.etOfficeAdress.setText(residentialAddress)
                binding.etOfficePincode.setText(residentialPinCode)
                binding.etOfficeCity.setText(residentialCity)
                binding.etOfficeDistrict.setText(residentialDistrict)
                binding.etOfficeState.setText(residentialState)
            }else{
                binding.etOfficeAdress.setText("")
                binding.etOfficePincode.setText("")
                binding.etOfficeCity.setText("")
                binding.etOfficeDistrict.setText("")
                binding.etOfficeState.setText("")
            }
        }

    }
    override fun setObserver() {
    }

    override fun setViewModel() {
    }

    private fun getData(){
        residentialPinCode = binding.etPincode.text.toString()
        residentialCity = binding.etCity.text.toString()
        residentialState = binding.etState.text.toString()
        residentialAddress = binding.etAddress.text.toString()
        officePinCode = binding.etOfficePincode.text.toString()
        officeCity = binding.etOfficeCity.text.toString()
        officeState = binding.etOfficeState.text.toString()
        officeAddress = binding.etOfficeAdress.text.toString()
        residentialDistrict = binding.etDistrict.text.toString()
        officeDistrict = binding.etOfficeDistrict.text.toString()

        //save data in preference
        setDataInPreference(this, Constant.RESIDENTIAL_PINCODE,residentialPinCode)
        setDataInPreference(this,Constant.RESIDENTIAL_CITY,residentialCity)
        setDataInPreference(this,Constant.RESIDENTIAL_STATE,residentialState)
        setDataInPreference(this,Constant.RESIDENTIAL_ADDRESS,residentialAddress)
        setDataInPreference(this,Constant.OFFICE_PIN_CODE,officePinCode)
        setDataInPreference(this,Constant.OFFICE_CITY,officeCity)
        setDataInPreference(this,Constant.OFFICE_STATE,officeState)
        setDataInPreference(this,Constant.OFFICE_ADDRESS,officeAddress)
        setDataInPreference(this,Constant.RESIDENTIAL_DISTRICT,residentialDistrict)
        setDataInPreference(this,Constant.OFFICE_DISTRICT,officeDistrict)

    }

    private fun validation(view: View){
        getData()
        if(residentialPinCode=="" || residentialPinCode.length < 6){
            showSnackbar(view,"Please enter residential pin code")
        }else if(residentialCity == ""){
            showSnackbar(view,"Please enter residential city name")
        }else if(residentialState == ""){
            showSnackbar(view,"Please enter residential state" )
        }else if(residentialAddress == ""){
            showSnackbar(view,"Please enter residetial address")
        }else if(officePinCode == "" || officePinCode.length < 6){
            showSnackbar(view,"Please enter office pin code")
        }else if(officeCity == ""){
            showSnackbar(view,"Please enter office city name")
        }else if(officeState == ""){
            showSnackbar(view,"Please enter office state")
        }else if(officeAddress == ""){
            showSnackbar(view,"Please enter office address")
        }else{
            HomeFragment.COMPLETED_PAGE_STATUS ="Address"
            finish()
        }
    }
}