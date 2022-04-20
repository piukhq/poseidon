package com.bink.localhero.model.bakery


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bakeries(
    @Json(name = "features")
    val features: List<Feature>,
    @Json(name = "type")
    val type: String
)