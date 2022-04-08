package com.bink.localhero.screens.wallet

import com.bink.localhero.data.remote.ApiService
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.model.wallet.UserWallet

class WalletRepository(private val apiService: ApiService) {

    suspend fun getPlans(): List<LoyaltyPlan> = apiService.getLoyaltyPlans()

    suspend fun getWallet(): UserWallet = apiService.getWallet()
}