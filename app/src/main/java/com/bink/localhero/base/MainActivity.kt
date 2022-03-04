package com.bink.localhero.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bink.localhero.R
import com.bink.localhero.di.roomModules
import com.bink.localhero.utils.Keys
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", Keys.testApiKey())

        startKoin {
            androidContext(this@MainActivity)
            modules(listOf(roomModules))
        }

    }
}