package com.example.logintask.dummysignup

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log


class TaskLogoutService : Service() {
    var timer: CountDownTimer? = null
    override fun onCreate() {
        super.onCreate()
        timer = object : CountDownTimer(1 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.v("TAG", "Service Started")
            }

            override fun onFinish() {
                Log.v("TAG", "Call Logout by Service")
                stopSelf()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}