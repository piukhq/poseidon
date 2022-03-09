package com.bink.localhero.model.loyalty_plan


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Journey(
    @Json(name = "description")
    val description: String?,
    @Json(name = "type")
    val type: Int?
)