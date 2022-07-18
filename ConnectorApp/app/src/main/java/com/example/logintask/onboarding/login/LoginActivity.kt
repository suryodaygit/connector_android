package com.example.logintask.onboarding.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.databinding.ActivityLoginBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.showSnackbar
import com.example.logintask.onboarding.signup.SignupActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var selectedLoginType = ""
    private lateinit var selectedRadioButton: RadioButton

    override fun getLayout() = R.layout.activity_login

    override fun init() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)

        /*  binding.button.btn.text = "LOGIN"
        binding.button.btn.setOnClickListener {
             if(binding.etEmail.text.toString()=="" || !checkEmail(binding.etEmail.text.toString())) {
                 showSnackbar(it,"Please enter valid email id")
             }else if(binding.etPassword.text.toString() =="" || binding.etPassword.text.length < 6){
                 showSnackbar(it,"Please enter password")
             }else{
                 showSnackbar(it,"Successfull Login.....")
                 startActivity(Intent(this,DashboardActivity::class.java))
             }
         }
 */
        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.tvNewUser.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.etEmailMobile.setOnClickListener {
            if (selectedLoginType == "") {
                binding.etEmailMobile.isFocusable = false
                binding.etEmailMobile.isClickable = false
                val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etEmailMobile.windowToken, 0)
                showSnackbar(it, "Please select login type Email or Mobile")
            }else{
                binding.etEmailMobile.isFocusable = true
                binding.etEmailMobile.isFocusableInTouchMode = true
            }
        }

        binding.rgLoginType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedbuttonId = radioGroup.checkedRadioButtonId
            if (selectedbuttonId != -1) {
                selectedRadioButton = radioGroup.findViewById(selectedbuttonId)
                selectedLoginType = selectedRadioButton.text.toString()
            }

            if (selectedLoginType == "Mobile") {
                binding.etEmailMobile.setText("")
                binding.etEmailMobile.inputType = InputType.TYPE_CLASS_NUMBER

                binding.etEmailMobile.addTextChangedListener(object : TextWatcher {

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

                        if (s.length == 10) {
                            sendOTPDialog(this@LoginActivity)
                        }
                    }
                })
            } else {
                binding.etEmailMobile.inputType = InputType.TYPE_CLASS_TEXT
                binding.ivVerifyMobile.visibility = View.GONE
                binding.etEmailMobile.setText("")
            }


        })

    }

    override fun setObserver() {
    }

    override fun setViewModel() {
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
            val mobileNo = "91" + binding.etEmailMobile.text.toString()
            if (mobileNo == "" || mobileNo == "91") {
                showSnackbar(it, "Please enter mobile no")
            } else {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        }

        btn_no.setOnClickListener {
            binding.ivVerifyMobile.visibility = View.VISIBLE
            alertDialog.dismiss()

        }

    }
}