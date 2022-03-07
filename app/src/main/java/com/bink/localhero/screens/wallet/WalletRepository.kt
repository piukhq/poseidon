package com.bink.localhero.screens.wallet

import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.network.ApiService

class WalletRepository(private val apiService: ApiService) {

    suspend fun getPlans(): List<LoyaltyPlan> {
        return apiService.getLoyaltyPlans()
    }
}