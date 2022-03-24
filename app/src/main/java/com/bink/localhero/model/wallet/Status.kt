package com.bink.localhero.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "description")
    val description: Any,
    @Json(name = "slug")
    val slug: Any,
    @Json(name = "state")
    val state: String
)