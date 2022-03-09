package com.bink.localhero.model.loyalty_plan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "column")
    val column: String?,
    @Json(name = "value")
    val value: String?
)
