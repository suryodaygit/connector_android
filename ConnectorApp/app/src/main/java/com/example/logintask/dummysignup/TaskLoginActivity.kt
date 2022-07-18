package com.example.logintask.dummysignup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.logintask.R
import com.example.logintask.databinding.ActivityLoginBinding


class TaskLoginActivity : AppCompatActivity() {

    private lateinit var bindingLoginActivity: ActivityLoginBinding
    private val EMAIL_KEY = "email_key"
    val PASSWORD_KEY = "password_key"
    val SHARED_PREFS = "shared_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_login)
        bindingLoginActivity = DataBindingUtil.setContentView(this, R.layout.activity_task_login)
        bindingLoginActivity.button.btn.setOnClickListener {
            if (bindingLoginActivity.etEmail.text.isEmpty()) {
                Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show()
            } else if (bindingLoginActivity.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            } else {
                val editor: SharedPreferences.Editor =
                    getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
                editor.putString(EMAIL_KEY, bindingLoginActivity.etEmail.text.toString())
                editor.putString(PASSWORD_KEY, bindingLoginActivity.etPassword.text.toString())
                editor.apply()
                startActivity(Intent(this, DummySignUpActivity::class.java))
            }
        }
    }
}