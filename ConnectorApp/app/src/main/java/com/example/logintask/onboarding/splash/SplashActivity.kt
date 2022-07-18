package com.example.logintask.onboarding.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Handler
import androidx.lifecycle.Observer
import com.example.logintask.R
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.obtainViewModel
import com.example.logintask.lib.utils.showSnackbar
import com.example.logintask.lib.utils.showToast
import com.example.logintask.onboarding.login.LoginActivity


class SplashActivity:BaseActivity() {
    private val SPLASH_SCREEN_TIME_OUT = 2000
    private lateinit var splashViewModel : SplashViewModel

    override fun getLayout() = R.layout.activity_splash

    override fun init() {

        val manager: PackageManager = this.getPackageManager()
        val info: PackageInfo = manager.getPackageInfo(
            this.getPackageName(), 0
        )
        val version = info.versionName
        val data = Data("Android",version)
        val requestModel = VersionRequestModel(data)
        splashViewModel.checkAppVersion(requestModel)

        Handler().postDelayed(Runnable {
            val i = Intent(
                this,
                LoginActivity::class.java
            )
            startActivity(i)
            finish()
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }

    override fun setObserver() {
     splashViewModel.getVersionData().observe(this, Observer {
         showToast(this,it.toString())
     })
    }

    override fun setViewModel() {
        splashViewModel = obtainViewModel(SplashViewModel::class.java)
    }
}