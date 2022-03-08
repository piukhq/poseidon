package com.bink.localhero.model.loyalty_plan

import android.net.wifi.hotspot2.pps.Credential
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddFields(
    @Json(name = "consents")
    val consents: List<Consent>?,
    @Json(name = "credentials")
    val credentials: List<Credential>?,
    @Json(name = "plan_documents")
    val planDocuments: List<PlanDocument>?
)