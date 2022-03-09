package com.bink.localhero.model.loyalty_plan


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlanDetails(
    @Json(name = "category")
    val category: String?,
    @Json(name = "company_name")
    val companyName: String?,
    @Json(name = "join_incentive")
    val joinIncentive: String?,
    @Json(name = "plan_description")
    val planDescription: String?,
    @Json(name = "plan_label")
    val planLabel: String?,
    @Json(name = "plan_name")
    val planName: String?,
    @Json(name = "plan_register_info")
    val planRegisterInfo: String?,
    @Json(name = "plan_summary")
    val planSummary: String?,
    @Json(name = "plan_url")
    val planUrl: String?,
    @Json(name = "redeem_instructions")
    val redeemInstructions: String?,
    @Json(name = "tiers")
    val tiers: List<Tier>?
)