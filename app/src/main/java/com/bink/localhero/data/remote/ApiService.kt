package com.bink.localhero.data.remote

import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import retrofit2.http.GET

interface ApiService {
    @GET("loyalty_plans")
    suspend fun getLoyaltyPlans():List<LoyaltyPlan>
}