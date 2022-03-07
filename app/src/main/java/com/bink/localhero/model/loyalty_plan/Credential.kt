package com.bink.localhero.model.loyalty_plan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credential(
    @Json(name = "alternative")
    val alternative: Alternative?,
    @Json(name = "choice")
    val choice: List<String>?,
    @Json(name = "credential_slug")
    val credentialSlug: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "display_label")
    val displayLabel: String?,
    @Json(name = "is_sensitive")
    val isSensitive: Boolean?,
    @Json(name = "order")
    val order: Int?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "validation")
    val validation: String?
)