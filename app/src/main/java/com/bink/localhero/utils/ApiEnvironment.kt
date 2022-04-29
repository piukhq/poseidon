package com.bink.localhero.utils

enum class ApiEnvironment(val url: String) {
    DEV("https://api.dev.gb.bink.com/v2/"),
    STAGING("https://api.staging.gb.bink.com/v2/")
}