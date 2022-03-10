package com.bink.localhero.model.loyalty_plan


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlanFeatures(
    @Json(name = "barcode_type")
    val barcodeType: Int?,
    @Json(name = "colour")
    val colour: String?,
    @Json(name = "has_points")
    val hasPoints: Boolean?,
    @Json(name = "has_transactions")
    val hasTransactions: Boolean?,
    @Json(name = "journeys")
    val journeys: List<Any>?,
    @Json(name = "plan_type")
    val planType: Int?,
    @Json(name = "text_colour")
    val textColour: Any?
)