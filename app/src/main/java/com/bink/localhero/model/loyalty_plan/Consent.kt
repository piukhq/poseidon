package com.bink.localhero.model.loyalty_plan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Consent(
    @Json(name = "consent_slug")
    val consentSlug: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "is_acceptance_required")
    val isAcceptanceRequired: Boolean?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "order")
    val order: Int?
)