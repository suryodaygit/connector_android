package com.example.logintask.dashboard.activity

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityBusinessDetailsBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.getPreferenceData
import com.example.logintask.lib.utils.setDataInPreference
import com.example.logintask.lib.utils.showSnackbar

class BusinessActivity : BaseActivity() {
    private lateinit var binding : ActivityBusinessDetailsBinding
    private var selectedEntity = ""
    private lateinit var selectedRadioButton: RadioButton
    private var msmeRegistrationNo=""
    private var msme=""
    private var ifscCode =""
    private var accountno =""
    private var bankName = ""
    private var branchName = ""
    private var accountHolderName = ""

    override fun getLayout() = R.layout.activity_business_details

    override fun init() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.toolbar.tvActivityTitle.text = "Business Details"
        binding.btnSubmit.btn.text = "Submit"
        setOnClickListner()
    }

    private fun setOnClickListner(){
        selectedEntity = getPreferenceData(this, Constant.SELECTED_ENTITY,"")

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

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSubmit.btn.setOnClickListener{
            validation(it)
        }
    }

    override fun setObserver() {
    }

    override fun setViewModel() {
    }

    private fun getData(){
        msmeRegistrationNo = binding.etMsmeNumber.text.toString()
        ifscCode = binding.etIfscNo.text.toString()
        accountno = binding.etAccountNo.text.toString()
        bankName = binding.etBankName.text.toString()
        branchName = binding.etBranchName.text.toString()
        accountHolderName = binding.etAccountHolderName.text.toString()

        setDataInPreference(this,Constant.MSME_REGISTRATION_NO,msmeRegistrationNo)
        setDataInPreference(this,Constant.IFSC_CODE,ifscCode)
        setDataInPreference(this,Constant.BANK_ACCOUNT_NO,accountno)
        setDataInPreference(this,Constant.BANK_NAME,bankName)
        setDataInPreference(this,Constant.BRANCH_NAME,branchName)
        setDataInPreference(this,Constant.ACCOUNT_HOLDER_NAME,accountHolderName)
    }

    private fun validation(view:View){
        getData()
       if(ifscCode==""){
           showSnackbar(view,"Please enter ifsc code")
       }else if(accountno==""){
           showSnackbar(view,"Please enter account no")
       }else if(bankName == ""){
           showSnackbar(view,"Please enter bank name")
       }else if(branchName == ""){
           showSnackbar(view,"Please enter branch name")
       }else if(accountHolderName == ""){
           showSnackbar(view,"Please enter account holder name")
       }else if(msme=="Yes" && msmeRegistrationNo ==""){
            showSnackbar(view,"Please enter msme registration no")
        }else{
            HomeFragment.COMPLETED_PAGE_STATUS ="Business"
            finish()
        }
    }
}