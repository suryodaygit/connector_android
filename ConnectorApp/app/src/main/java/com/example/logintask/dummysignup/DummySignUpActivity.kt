package com.example.logintask.dummysignup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.logintask.lib.utils.obtainViewModel
import com.example.logintask.R
import com.example.logintask.databinding.ActivitySignupBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.showSnackbar
import com.example.logintask.lib.utils.showToast
import com.example.logintask.onboarding.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*

class DummySignUpActivity : BaseActivity(),AdapterView.OnItemSelectedListener {
    val SHARED_PREFS = "shared_prefs"
    private lateinit var binding : ActivitySignupBinding
    private lateinit var mViewModel: DummySignUpViewModel
    private val entity = arrayOf("Select Entity", "Entity 1", "Entity 1", "Entity 1")
    private val name = arrayOf("Title", "Title", "Title", "Title")
    private val professionNetwork = arrayOf("Select","select","Select")
    private val preferedLang = arrayOf("Select Language","language","select")
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var mTimePicker: TimePickerDialog
    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)
    private var isMeeting = false
    private var birthDate=""
    var bsmId = ""
    var msme=""
    var icic=""
    var maritialStatus=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,getLayout())
        binding.tvScrolling.isSelected = true
        setSpinnerAdapter()
        setOnclickListener()
    }

    private fun setOnclickListener(){
        binding.tvCallDateText.setOnClickListener {
            isMeeting = false
            openDatepicker()
        }

        binding.tvMeetingDateText.setOnClickListener {
            isMeeting = true
            openDatepicker()
        }

        binding.tvCallTimeText.setOnClickListener {
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    binding.tvCallTimeText.setText(String.format("%d : %d", hourOfDay, minute))
                }
            }, hour, minute, false)
            mTimePicker.show()
        }

        binding.tvMeetingTimeText.setOnClickListener {
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    binding.tvMeetingTimeText.setText(String.format("%d : %d", hourOfDay, minute))
                }
            }, hour, minute, false)

            mTimePicker.show()
        }

        binding.tvBirthDateText.setOnClickListener {
            isMeeting = false
            birthDate = "Birth Date"
            openDatepicker()
        }


        binding.rgBsmId.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            bsmId =i.toString()
        })

        binding.rgMsme.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            msme =i.toString()
        })

        binding.rgIcici.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            icic =i.toString()
        })

        binding.rgMaritialStatus.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            maritialStatus =i.toString()
        })

        binding.btnSubmit.setOnClickListener {
            if(binding.spEntity.selectedItem == ""){
                showToast(this,"Please select entity.")
            }else if(binding.spName.selectedItem ==""){
                showToast(this,"Please select name type")
            }else if(binding.etName.text.toString()=="Name" || binding.etName.text.toString() ==""){
                showToast(this,"Please enter name")
            }else if(binding.etMobileText.text.toString()=="" || binding.etMobileText.text.length <10 ){
                showToast(this,"Please enter mobile no.")
            }else if(binding.etEmailText.text.toString() ==""){
                showToast(this,"Please enter email")
            }else if(binding.tvBirthDateText.text ==""){
                showToast(this,"Please enter birth date")
            }else if(binding.etPincodeText.text.toString() ==""){
                showToast(this,"Please enter pincode")
            }else if(binding.spProffessionNetwork.selectedItem ==""){
                showToast(this,"Please select proffessional network")
            }else if(binding.tvMeetingDateText.text.toString()==""){
                showToast(this,"Please select meeting date")
            }else if(binding.tvMeetingTimeText.text.toString()==""){
                showToast(this,"Please enter meeting time")
            }else{
                val selectedEntity = binding.spEntity.selectedItem.toString()
                val name = binding.etName.text.toString()
                val nameType= binding.spName.selectedItem.toString()
                val mobile = binding.etMobileText.text.toString()
                val alternateMobileNo = binding.etAlternetMobileNo.text.toString()
                val email= binding.etEmailText.text.toString()
                val DOB = binding.tvBirthDateText.text.toString()
                val aadharCardNo = binding.etAadharCardNo.text.toString()
                val pinCode= binding.etPincodeText.text.toString()
                val city = binding.etCityText.text.toString()
                val state = binding.etStateText.text.toString()
                val address=  binding.etAddressText.text.toString()
                val proffessionNetwork = binding.spProffessionNetwork.selectedItem.toString()
                val preferedDate = binding.tvCallDateText.text.toString()
                val preferedTime= binding.tvCallTimeText.text.toString()
                val meetingDate  = binding.tvMeetingDateText.text.toString()
                val meetingTime = binding.tvMeetingTimeText.text.toString()
                showToast(this,"Data submitted sucessfully")
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.etName.setText("")
            binding.etMobileText.setText("")
            binding.etAlternetMobileNo.setText("")
            binding.etEmailText.setText("")
            binding.tvBirthDateText.setText("")
            binding.etAadharCardNo.setText("")
            binding.etPincodeText.setText("")
            binding.etCityText.setText("")
            binding.etStateText.setText("")
            binding.etAddressText.setText("")
            binding.tvCallDateText.setText("")
            binding.tvCallTimeText.setText("")
            binding.tvMeetingDateText.setText("")
            binding.tvMeetingTimeText.setText("")

            startActivity(Intent(this,LoginActivity::class.java))

        }
    }

    private fun setSpinnerAdapter() {
        val entityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, entity)
        binding.spEntity.adapter = entityAdapter
        binding.spEntity.onItemSelectedListener = this
        entityAdapter.notifyDataSetChanged()

        val nameAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, name)
        binding.spName.adapter = nameAdapter
        binding.spName.onItemSelectedListener = this
        nameAdapter.notifyDataSetChanged()

        val proffessionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, professionNetwork)
        binding.spProffessionNetwork.adapter = proffessionAdapter
        binding.spProffessionNetwork.onItemSelectedListener = this
        proffessionAdapter.notifyDataSetChanged()

        val prefferedLangAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, preferedLang)
        binding.spPrefferedLang.adapter = prefferedLangAdapter
        binding.spPrefferedLang.onItemSelectedListener = this
        prefferedLangAdapter.notifyDataSetChanged()


    }
    override fun getLayout() = R.layout.activity_signup

    override fun init() {
    }

    @SuppressLint("SetTextI18n")
    fun openDatepicker() {
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val selectedMonth = month + 1
                // Display Selected date in TextView
                if (isMeeting){
                    binding.tvMeetingDateText.text =  "$dayOfMonth/$selectedMonth/$year"
                }else if( birthDate =="Birth Date"){
                    binding.tvBirthDateText.text = "$dayOfMonth/$selectedMonth/$year"
                    }
                else{
                        binding.tvCallDateText.text = "$dayOfMonth/$selectedMonth/$year"
                }
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("HH:mm:ss")
                val currentTime = mdformat.format(calendar.getTime())
              var  expDate = "$dayOfMonth-$selectedMonth-$year $currentTime"

            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    override fun setObserver() {

    }

    override fun setViewModel() {
        mViewModel = obtainViewModel(DummySignUpViewModel::class.java)
    }

    private fun startTimeCounter() {
        object : CountDownTimer(900000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                val editor: SharedPreferences.Editor =  getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
                editor.clear()
                editor.apply()
                Toast.makeText(this@DummySignUpActivity,"Session expire..",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DummySignUpActivity, TaskLoginActivity::class.java))
            }
        }.start()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
       // binding.spEntity.setSelection(p0?.getItemAtPosition(p2)).toInt()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}