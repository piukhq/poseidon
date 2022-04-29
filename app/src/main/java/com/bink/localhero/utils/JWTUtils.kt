package com.bink.localhero.utils

import android.util.Base64
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

object JWTUtils {

    fun decode(jwt: String): String? {
        try {
            val split = jwt.split("\\.".toRegex()).toTypedArray()
            return getJson(split[1])
        } catch (e: UnsupportedEncodingException) {
        }

        return null
    }

    fun getEmailFromJson(token: String): String? {
        return try {
            JSONObject(token).getString("user_id")
        } catch (e: JSONException) {
            null
        }

    }

    private fun getJson(strEncoded: String): String {
        val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes)
    }

}