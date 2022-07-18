package com.example.logintask.lib.utils

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logintask.R
import com.example.logintask.onboarding.login.LoginActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern


val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
/**
 * Print message in Toast
 */
fun showMessage(context:Context,msg:String){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}

/**
 * Extension method to get View Model for Activity.
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this).get(viewModelClass)

/*
* Check the email validation
*/
fun checkEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}


fun showSnackbar(view: View, msg:String){
    val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setPadding(10, 0, 10, 10)
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackbar.show()
}

fun setDataInPreference(mContext:Context,key:String,value:String) {
    val sharedPreference = mContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString(key,value)
    editor.apply()
}

fun getPreferenceData(mContext:Context,key:String,defaultString:String):String{
    val sharedPreference = mContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val value = sharedPreference.getString(key,defaultString)
    return value.toString()
}

fun showToast(context:Context,msg:String){
    Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
}

 fun logoutDialog(mContext: Context){
    val builder = android.app.AlertDialog.Builder(mContext)
    val view = LayoutInflater.from(mContext).inflate(
        R.layout.custom_logout_dialog,null)
    builder.setView(view)
    val btn_yes = view.findViewById<Button>(R.id.btn_yes)
    val btn_no = view.findViewById<Button>(R.id.btn_no)

    val alertDialog = builder.create()
    alertDialog.show()

    btn_yes.setOnClickListener {
        alertDialog.dismiss()
        mContext.startActivity(Intent(mContext,LoginActivity::class.java))
    }

    btn_no.setOnClickListener {
        alertDialog.dismiss()
    }



}

