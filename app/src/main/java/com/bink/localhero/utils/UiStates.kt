package com.bink.localhero.utils

import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.model.wallet.UserWallet

sealed class WalletUiState {
    object Loading : WalletUiState()
    data class Error(val exception: Exception?) : WalletUiState()
    data class ShowPlans(val plans: List<LoyaltyPlan>) : WalletUiState()
    data class ShowWallet(val wallet: UserWallet): WalletUiState()
}

sealed class AddPaymentCardUiState {
    object Loading : AddPaymentCardUiState()
    data class Error(val exception: Exception?) : AddPaymentCardUiState()
    object Success : AddPaymentCardUiState()
}
