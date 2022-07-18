package com.example.logintask.dashboard.activity

import android.content.Intent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.databinding.ActivityReviewBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.getPreferenceData

class ReviewActivity : BaseActivity() {

    private lateinit var binding : ActivityReviewBinding
    override fun getLayout() = R.layout.activity_review

    override fun init() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.toolbar.tvActivityTitle.text = "Review"
        binding.btnSubmit.btn.text = "Submit"
        setOnclickListener()
        setData()
    }

    private fun  setOnclickListener(){
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.btnSubmit.btn.setOnClickListener {
           finish()
        }
    }

    private fun setData(){
        binding.etEntity.setText(getPreferenceData(this,Constant.ENTITY,""))
        binding.etCompanyName.setText(getPreferenceData(this,Constant.COMPANY_NAME,""))
        binding.etPanCardNumber.setText(getPreferenceData(this,Constant.PAN_CARD_NO,""))
        binding.etName.setText(getPreferenceData(this,Constant.NAME,""))
        binding.etMobileNumber.setText(getPreferenceData(this,Constant.MOBILE_NO,""))
        binding.etAlternetMobileNumber.setText(getPreferenceData(this,Constant.ALTERNATE_MOBILE_NO,""))
        binding.etEmail.setText(getPreferenceData(this,Constant.EMAIL,""))
        binding.etAadharCardNo.setText(getPreferenceData(this,Constant.AADHAR_CARD_NO,""))
        binding.etGstNo.setText(getPreferenceData(this,Constant.GST_NO,""))
        binding.tvDob.text = getPreferenceData(this,Constant.DOB,"")
        binding.etPincode.setText(getPreferenceData(this,Constant.RESIDENTIAL_PINCODE,""))
        binding.etCity.setText(getPreferenceData(this,Constant.RESIDENTIAL_CITY,""))
        binding.etState.setText(getPreferenceData(this,Constant.RESIDENTIAL_STATE,""))
        binding.etAddress.setText(getPreferenceData(this,Constant.RESIDENTIAL_ADDRESS,""))
        binding.etOfficePincode.setText(getPreferenceData(this,Constant.OFFICE_PIN_CODE,""))
        binding.etOfficeCity.setText(getPreferenceData(this,Constant.OFFICE_CITY,""))
        binding.etOfficeState.setText(getPreferenceData(this,Constant.OFFICE_STATE,""))
        binding.etOfficeAdress.setText(getPreferenceData(this,Constant.OFFICE_ADDRESS,""))
        binding.etResidentialDistrict.setText(getPreferenceData(this,Constant.RESIDENTIAL_DISTRICT,""))
        binding.etOfficeDistrict.setText(getPreferenceData(this,Constant.OFFICE_DISTRICT,""))
        binding.etPresentOccupation.setText(getPreferenceData(this,Constant.PRESENT_OCCUPATION,""))
        binding.tvDate.text = getPreferenceData(this,Constant.CALLING_DATE,"")
        binding.tvTime.text = getPreferenceData(this,Constant.CALLING_TIME,"")
        binding.etPrefferedLanguage.setText(getPreferenceData(this,Constant.PREFERRED_LANGUAGE,""))
        binding.etBankCustId.setText(getPreferenceData(this,Constant.SURYODAY_CUSTOMER_ID,""))
        binding.tvMeetingDate.text = getPreferenceData(this,Constant.MEETING_DATE,"")
        binding.tvMeetingTime.text = getPreferenceData(this,Constant.MEETING_TIME,"")
        binding.etStaffName.setText(getPreferenceData(this,Constant.STAFF_NAME,""))
        binding.etRelationWithStaff.setText(getPreferenceData(this,Constant.RELATIONSHIP_WITH_STAFF,""))

        if(getPreferenceData(this,Constant.MSME_CHECK,"") == "Yes"){
            binding.rgMsmeYes.isChecked = true
            binding.llMsmeNo.visibility = View.VISIBLE
            binding.etMsmeNumber.setText(getPreferenceData(this,Constant.MSME_REGISTRATION_NO,""))
        }else{
            binding.rgMsmeNo.isChecked = true
            binding.llMsmeNo.visibility = View.GONE
        }

        if(getPreferenceData(this,Constant.BSM_CHECK,"") == "Yes"){
            binding.llBsmNo.visibility = View.VISIBLE
            binding.rgBsmYes.isChecked = true
            binding.etBsmNumber.setText(getPreferenceData(this,Constant.BSM_ID,""))
        }else{
            binding.llBsmNo.visibility = View.GONE
            binding.rgBsmNo.isChecked = true
        }

        if(getPreferenceData(this,Constant.ACCOUNT_SURYODAY_BANK_CHECK,"") == "Yes"){
            binding.llSuryodayBankAccount.visibility = View.VISIBLE
            binding.rgBankYes.isChecked = true
        }else{
            binding.llSuryodayBankAccount.visibility = View.GONE
            binding.rgBankNo.isChecked = true
        }

        if(getPreferenceData(this,Constant.SSFB_STAFF_CHECK,"") == "Yes"){
            binding.llSsfbStaff.visibility = View.VISIBLE
            binding.rgSsfbYes.isChecked  = true
        }else{
            binding.llSsfbStaff.visibility = View.GONE
            binding.rgSsfbNo.isChecked = true
        }
    }
    override fun setObserver() {
    }

    override fun setViewModel() {
    }
}