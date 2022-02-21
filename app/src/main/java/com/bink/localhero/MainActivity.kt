package com.bink.localhero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bink.localhero.utils.Keys

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity",Keys.testApiKey())
    }
}