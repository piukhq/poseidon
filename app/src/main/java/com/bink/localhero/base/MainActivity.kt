package com.bink.localhero.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bink.localhero.R
import com.bink.localhero.di.networkModule
import com.bink.localhero.di.roomModules
import com.bink.localhero.di.viewModelModule
import com.bink.localhero.utils.Keys
import com.bink.localhero.utils.LocalStoreUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", Keys.testApiKey())
    }

    fun forceRunApp() {
        LocalStoreUtils.clearPreferences(this)
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_NEW_TASK
        )
        finishAffinity()
        startActivity(launchIntent)
        exitProcess(0)
    }
}