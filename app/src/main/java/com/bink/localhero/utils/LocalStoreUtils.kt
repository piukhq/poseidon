package com.bink.localhero.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object LocalStoreUtils {

    private const val PREF_FILE_NAME = "com.bink.localhero"
    const val KEY_TOKEN = "encrypted_token"
    private lateinit var encryptedSharedPreferences: SharedPreferences

    fun createEncryptedPrefs(context: Context) {
        val masterKeyBuilder =
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        val masterKey = masterKeyBuilder.build()
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setAppSharedPref(secretKey: String, secret: String) {
        try {
            encryptedSharedPreferences.edit().let {
                it.putString(secretKey, secret)
                it.apply()
                it.commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAppSharedPref(secretKey: String): String? {
        try {
            return encryptedSharedPreferences.getString(secretKey, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun removeKey(secretKey: String) {
        encryptedSharedPreferences.edit().let {
            it.remove(secretKey)
            it.apply()
        }
    }
}