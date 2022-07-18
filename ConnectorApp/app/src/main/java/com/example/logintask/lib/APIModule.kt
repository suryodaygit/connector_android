package com.example.logintask.lib

import com.example.logintask.application.MainApplication
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import javax.inject.Singleton

/**
 * Created by komal Ardekar
 */
@Module
class APIModule(
    @get:Provides
    @get:Singleton
    internal val app: MainApplication
)
