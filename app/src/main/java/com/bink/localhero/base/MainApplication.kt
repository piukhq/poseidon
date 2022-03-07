package com.bink.localhero.base

import android.app.Application
import com.bink.localhero.di.networkModule
import com.bink.localhero.di.roomModules
import com.bink.localhero.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(roomModules,networkModule, viewModelModule)
        }
    }
}