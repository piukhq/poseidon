package com.bink.localhero.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JourneyFields(
    @Json(name = "add_fields")
    val addFields: Any?,
    @Json(name = "authorise_fields")
    val authoriseFields: Any?,
    @Json(name = "join_fields")
    val joinFields: Any?,
    @Json(name = "register_ghost_card_fields")
    val registerGhostCardFields: Any?
)