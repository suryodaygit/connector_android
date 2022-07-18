package com.example.logintask.onboarding.signup

import APICallbacks
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call

class SignUpViewModel: ViewModel() {
    private var otpData: MutableLiveData<SendOTPResponseModel> = MutableLiveData()
    fun getSendOTPData(): LiveData<SendOTPResponseModel> = otpData
    private var validateOtpData: MutableLiveData<ValidateOTPResponseModel> = MutableLiveData()
    fun getValidateOTPData(): LiveData<ValidateOTPResponseModel> = validateOtpData
    var errorMsg = MutableLiveData<String>()

    fun sendOtp(mContext: Context, sendOtp: SendOTPRequestModel) {
        LoadingDialog.showLoading(mContext)
        APIService.getBaseUrl()
            .sendMobileOTP(
                 "DIG123456789056",
                "NOVOPAY",
                "S7050",
                "CB",
                "NOVOPAY",
                "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4",
                "application/json",
                "kyqak5muymxcrjhc5q57vz9v", sendOtp)
            .enqueue(object : APICallbacks<SendOTPResponseModel>() {
                override fun onSuccess(response: SendOTPResponseModel?) {
                    LoadingDialog.hideLoading()
                    otpData.postValue(response!!)
                }

                override fun onFailure(generalErrorMsg: String, systemErrorMsg: String) {
                    LoadingDialog.hideLoading()
                    errorMsg.postValue(generalErrorMsg)
                }

                override fun onFailure(call: Call<SendOTPResponseModel>, t: Throwable) {
                    LoadingDialog.hideLoading()
                    errorMsg.postValue(t.message)
                }
            })
    }

    fun validateOTP(X_correlation_ID:String,otp:String){
        APIService.getBaseUrl()
            .validateMobileOTP(
                "kyqak5muymxcrjhc5q57vz9v",
                 otp,
                "D",
                X_correlation_ID,
                "HOTFOOT_UAT",
            "S7171",
            "CB",
            "HOTFOOT_UAT",
                "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4",
            "application/json")
            .enqueue(object : APICallbacks<ValidateOTPResponseModel>(){
                override fun onSuccess(response: ValidateOTPResponseModel?) {
                }

                override fun onFailure(generalErrorMsg: String, systemErrorMsg: String) {
                }

                override fun onFailure(call: Call<ValidateOTPResponseModel>, t: Throwable) {
                }

            })
    }
}