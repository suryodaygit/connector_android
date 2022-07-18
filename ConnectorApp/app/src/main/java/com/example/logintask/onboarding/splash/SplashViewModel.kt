package com.example.logintask.onboarding.splash

import APICallbacks
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call

class SplashViewModel: ViewModel() {
    private var versionData: MutableLiveData<CheckVersionModel> = MutableLiveData()
    fun getVersionData(): LiveData<CheckVersionModel> = versionData
    var errorMsg = MutableLiveData<String>()

    fun checkAppVersion(data:VersionRequestModel)
    {
        APIService.getBaseUrl()
            .checkVersion(" DIG123456789056",
                 "AOCPV",
                     "123",
                "123",
                "345",
                "AOCPV",
                "application/json",
                "HttpOnly",data)
            .enqueue(object : APICallbacks<CheckVersionModel>() {
                override fun onSuccess(response: CheckVersionModel?) {
                    versionData.postValue(response!!)
                }

                override fun onFailure(generalErrorMsg: String, systemErrorMsg: String) {
                    errorMsg.postValue(generalErrorMsg)
                }

                override fun onFailure(call: Call<CheckVersionModel>?, t: Throwable?) {
                }
            })

    }

}