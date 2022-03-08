package com.bink.localhero.screens.wallet

import com.bink.localhero.data.remote.ApiService
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan

class WalletRepository(private val apiService: ApiService) {

    suspend fun getPlans(): List<LoyaltyPlan> {
        return apiService.getLoyaltyPlans()
    }
}