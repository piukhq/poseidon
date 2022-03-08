package com.bink.localhero.model.loyalty_plan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlanDocument(
    @Json(name = "description")
    val description: String?,
    @Json(name = "is_acceptance_required")
    val isAcceptanceRequired: Boolean?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "order")
    val order: Int?,
    @Json(name = "url")
    val url: String?
)