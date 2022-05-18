package com.bink.localhero.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceManager {

    private const val FILE_NAME = "LocalHeroPrefs"
    private const val ENV_FILE_NAME = "LocalHeroEnvPrefs"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var environmentPreferences: SharedPreferences

    private const val API_ENV = "apiEnv"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(FILE_NAME, MODE)
        environmentPreferences = context.getSharedPreferences(ENV_FILE_NAME, MODE)
    }

    var storedApiUrl: String?
        get() = environmentPreferences.getString(API_ENV, null)
        set(value) = environmentPreferences.edit {
            it.putString(API_ENV, value)
        }

    fun clear(){
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

}