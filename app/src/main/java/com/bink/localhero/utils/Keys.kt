package com.bink.localhero.utils

object Keys {
    init {
        System.loadLibrary("native-lib")
    }

    external fun testApiKey(): String
    external fun spreedlyKey(): String
    external fun binkTestAuthToken(): String
}