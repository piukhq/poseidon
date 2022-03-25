package com.bink.localhero.model.loyalty_plan


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JourneyFields(
    @Json(name = "add_fields")
    val addFields: AddFields?,
    @Json(name = "authorise_fields")
    val authoriseFields: AuthoriseFields?,
    @Json(name = "join_fields")
    val joinFields: JoinFields?,
    @Json(name = "register_ghost_card_fields")
    val registerGhostCardFields: RegisterGhostCardFields?
)