package com.example.healthqrapp.lib.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.util.Patterns
import androidx.databinding.ktx.BuildConfig
import com.example.logintask.application.MainApplication
import com.example.logintask.R
import java.io.File

object Utility {

    /**
     * Prints message in logcat.
     * Only for debug mode.
     *
     * @param msg message to be printed
     */
    fun printLog(msg: String) {
        if (BuildConfig.DEBUG)
            Log.d(MainApplication.instance.getContext().getString(R.string.app_name), "SSM : $msg")
    }

    /**
     * Check if device is connected to an active network.
     *
     * @return True if device is connected to an active network
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            MainApplication.instance.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.apply {
                return getNetworkCapabilities(activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } ?: false
            }
        } else {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    /**
     * Check if file is created and contains some data .
     *
     * @param fileName File name to check
     * @return True if file is created and contains some data
     */
    fun isFileCreated(fileName: String): Boolean {
        val file = File(MainApplication.instance.getContext().filesDir, fileName)
        return file.exists() && file.length() > 0
    }

    /**
     * Check if the email is Invalid.
     *
     * @param email Email
     * @return True if the Email is Invalid
     */
    fun isEmailInvalid(email: String): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Store string in preferences file.
     *
     * @param key   Preferences Key
     * @param value String to be stored
     */
    fun addPreference(context:Context,key: String, value: String) {
        val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        val editor =sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    /**
     * Retrieve string from preferences file.
     *
     * @param key Preferences Key
     * @return String from preferences file based on Key
     */
    fun getPreference(key: String): String? {
        return null
    }
}