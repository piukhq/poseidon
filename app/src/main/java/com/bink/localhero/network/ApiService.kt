package com.bink.localhero.network

import com.bink.localhero.model.LoyaltyPlan
import retrofit2.http.GET

interface ApiService {
    @GET("/loyalty_plans")
    suspend fun getLoyaltyPlans():List<LoyaltyPlan>
}