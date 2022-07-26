package com.example.logintask.dashboard.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityOtherBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.setDataInPreference
import com.example.logintask.lib.utils.showSnackbar
import java.util.*

class OtherActivity : BaseActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var binding: ActivityOtherBinding
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var mTimePicker: TimePickerDialog
    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)
    private var bsm=""
    private val relationWithStaff = arrayOf ("Spouse","Father","Mother","Son","Daughter","Father-in-law","Mother-in-law",
            "Brother","Sister","Friend","Relative")
    private var bankAccount=""
    private var relationWithBank=""
    private lateinit var selectedRadioButton: RadioButton
    private var callingDate = ""
    private var callingTime = ""
    private var preferredLang = ""
    private var bankCustId = ""
    private var meetingDate = ""
    private var meetingTime = ""
    private var isMeeting = false
    private var suryodayAccountCheck = ""
    private var ssfbStaffCheck = ""
    private var ssfbStaff = ""
    private var ssfbStaffName = ""
    private var relationshipStaff = ""
    private var bsmId=""
    override fun getLayout() = R.layout.activity_other

    override fun init() {
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.toolbar.tvActivityTitle.text = "Other Details"
        binding.btnSubmit.btn.text = "Submit"
        setOnclickListner()
        setSpinner()
    }

    @SuppressLint("SetTextI18n")
    private fun setOnclickListner(){
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.llMeetingDate.setOnClickListener {
            isMeeting = true
            openDatepicker()
        }

        binding.llMeetingTime.setOnClickListener {
            isMeeting = true
            openTimePicker()
        }

        binding.rgSuryodayBankAccount.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId =radioGroup.checkedRadioButtonId
            if(selectedbuttonId !=-1){
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                bankAccount = selectedRadioButton.text.toString()
            }
            if(bankAccount=="Yes"){
                suryodayAccountCheck = "Yes"
                binding.llSuryodayBankAccount.visibility = View.VISIBLE
            }else{
                suryodayAccountCheck = "No"
                binding.llSuryodayBankAccount.visibility = View.GONE
            }
        })

        binding.rgExistingSsfb.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId =radioGroup.checkedRadioButtonId

            if(selectedbuttonId !=-1){
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                relationWithBank = selectedRadioButton.text.toString()
            }
            if(relationWithBank=="Yes"){
                ssfbStaffCheck = "Yes"
                binding.llSsfbStaff.visibility = View.VISIBLE
            }else{
                ssfbStaffCheck = "No"
                binding.llSsfbStaff.visibility = View.GONE
            }
        })

        binding.btnSubmit.btn.setOnClickListener {
            validation(it)
        }

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

    }

    private fun setSpinner(){
        val relationAdapter = ArrayAdapter(this, R.layout.spinner_list, relationWithStaff)
        binding.spRelationshipWithStaff.adapter = relationAdapter
        binding.spRelationshipWithStaff.onItemSelectedListener = this
        relationAdapter.notifyDataSetChanged()
    }

    private fun openTimePicker(){
        mTimePicker = TimePickerDialog(this,
            { _, hourOfDay, minute ->
                val am_pm: String = if (hourOfDay < 12) {
                    "AM"
                } else {
                    "PM"
                }
                val time  = String.format("%d : %d", hourOfDay, minute)
                if (isMeeting){
                    binding.tvMeetingTime.text =  "$time $am_pm"
                }else {
                }
            }, hour, minute, false)

        mTimePicker.show()
    }

    @SuppressLint("SetTextI18n")
    fun openDatepicker() {
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val selectedMonth = month + 1
                if(isMeeting){
                    binding.tvMeetingDate.text = "$dayOfMonth/$selectedMonth/$year"
                }else {
                }
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    private fun getData(){
        bankCustId = binding.etBankCustId.text.toString()
        meetingDate = binding.tvMeetingDate.text.toString()
        meetingTime = binding.tvMeetingTime.text.toString()
        ssfbStaffName = binding.etStaffName.text.toString()
        bsmId = binding.etBsmNumber.text.toString()


        // save data in preference
        setDataInPreference(this,Constant.CALLING_TIME,callingTime)
        setDataInPreference(this,Constant.CALLING_DATE,callingDate)
        setDataInPreference(this,Constant.PREFERRED_LANGUAGE,preferredLang)
        setDataInPreference(this,Constant.SURYODAY_CUSTOMER_ID,bankCustId)
        setDataInPreference(this,Constant.MEETING_DATE,meetingDate)
        setDataInPreference(this,Constant.MEETING_TIME,meetingTime)
        setDataInPreference(this,Constant.STAFF_NAME,ssfbStaffName)
        setDataInPreference(this,Constant.RELATIONSHIP_WITH_STAFF,relationshipStaff)
        setDataInPreference(this,Constant.ACCOUNT_SURYODAY_BANK_CHECK,suryodayAccountCheck)
        setDataInPreference(this,Constant.SSFB_STAFF_CHECK,ssfbStaffCheck)
        setDataInPreference(this,Constant.BSM_ID,bsmId)

    }

    private fun validation(view:View){
        getData()
        if(suryodayAccountCheck == "Yes" && bankCustId == "" ){
            showSnackbar(view,"Please enter suryoday bank customer ID")
        }else if(ssfbStaffCheck == "Yes" && ssfbStaffName == "") {
            showSnackbar(view, "Please enter staff name")
        }else if (ssfbStaffCheck == "Yes" && relationshipStaff == "") {
            showSnackbar(view, "Please enter relationship with staff")
        }else if(bsm=="Yes" && bsmId == ""){
            showSnackbar(view,"Please enter bsm registration Id")
        }else{
            HomeFragment.COMPLETED_PAGE_STATUS = "Other"
            finish()
        }
    }
    override fun setObserver() {
    }

    override fun setViewModel() {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        relationshipStaff = binding.spRelationshipWithStaff.selectedItem.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}