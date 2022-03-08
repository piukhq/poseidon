package com.bink.localhero.model.loyalty_plan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "description")
    val description: String?,
    @Json(name = "encoding")
    val encoding: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "type")
    val type: Int?,
    @Json(name = "url")
    val url: String?
)