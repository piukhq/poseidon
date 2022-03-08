package com.bink.localhero.model.loyalty_plan


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoyaltyPlan(
    @Json(name = "content")
    val content: List<Any>?,
    @Json(name = "images")
    val images: List<Any>?,
    @Json(name = "is_in_wallet")
    val isInWallet: Boolean?,
    @Json(name = "journey_fields")
    val journeyFields: JourneyFields?,
    @Json(name = "loyalty_plan_id")
    val loyaltyPlanId: Int?,
    @Json(name = "plan_details")
    val planDetails: PlanDetails?,
    @Json(name = "plan_features")
    val planFeatures: PlanFeatures?,
    @Json(name = "plan_popularity")
    val planPopularity: Any?
)