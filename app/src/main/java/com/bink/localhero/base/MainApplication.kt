package com.bink.localhero.base

import android.app.Application
import com.bink.localhero.di.networkModule
import com.bink.localhero.di.roomModules
import com.bink.localhero.di.spreedlyModule
import com.bink.localhero.di.viewModelModule
import com.bink.localhero.utils.ApiConstants
import com.bink.localhero.utils.LocalStoreUtils
import com.bink.localhero.utils.SharedPreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalStoreUtils.createEncryptedPrefs(applicationContext)
        SharedPreferenceManager.init(this@MainApplication)
        if (SharedPreferenceManager.storedApiUrl.isNullOrEmpty()) {
            SharedPreferenceManager.storedApiUrl = ApiConstants.BASE_URL
        } else {
            ApiConstants.BASE_URL = SharedPreferenceManager.storedApiUrl.toString()
        }
        startKoin {
            androidContext(this@MainApplication)
            modules(roomModules, networkModule, viewModelModule, spreedlyModule)
        }
    }
}