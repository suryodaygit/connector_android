package com.example.logintask.lib

import com.example.logintask.application.MainApplication
import com.google.android.datatransport.runtime.dagger.Component
import javax.inject.Singleton

/**
 * Created by Nikhil Nyayadhish on 06 Aug 2019 at 18:37.
 */
@Singleton
@Component(modules = [APIModule::class])
interface APIComponent {
    val app: MainApplication
}
