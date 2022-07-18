package com.example.logintask.application

import android.app.Application
import android.content.Context
import com.example.logintask.lib.APIComponent


/**
 * Application class is used to get live context throughout the app.
 * Also used to register callbacks at the start of the application.
 *
 * @author Komal Ardekar
 */
class MainApplication : Application() {
    companion object {
      lateinit var instance: MainApplication
        lateinit var apiComponent: APIComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    /**
     * @return The live application context.
     */
    fun getContext(): Context {
        return applicationContext
    }

    /**
     * @return Instance of APIComponent.
     */
    fun getAPIComponent(): APIComponent {
        return apiComponent
    }
}