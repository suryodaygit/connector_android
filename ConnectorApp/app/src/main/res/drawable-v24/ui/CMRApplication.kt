package com.app.cmrk

import android.app.Application

open class CMRApplication : Application() {


    companion object {
        lateinit var instance: CMRApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}