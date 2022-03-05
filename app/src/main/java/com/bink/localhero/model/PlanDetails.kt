package com.bink.localhero.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlanDetails(
    @Json(name = "category")
    val category: String?,
    @Json(name = "company_name")
    val companyName: String?,
    @Json(name = "join_incentive")
    val joinIncentive: Any?,
    @Json(name = "plan_description")
    val planDescription: Any?,
    @Json(name = "plan_label")
    val planLabel: Any?,
    @Json(name = "plan_name")
    val planName: String?,
    @Json(name = "plan_register_info")
    val planRegisterInfo: Any?,
    @Json(name = "plan_summary")
    val planSummary: Any?,
    @Json(name = "plan_url")
    val planUrl: String?,
    @Json(name = "redeem_instructions")
    val redeemInstructions: Any?,
    @Json(name = "tiers")
    val tiers: List<Any>?
)