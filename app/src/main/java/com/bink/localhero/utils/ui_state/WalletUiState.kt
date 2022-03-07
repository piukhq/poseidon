package com.bink.localhero.utils.ui_state

import com.bink.localhero.model.loyalty_plan.LoyaltyPlan

sealed class WalletUiState {
    object Loading : WalletUiState()
    data class Error(val message: String?) : WalletUiState()
    data class Success(val plans: List<LoyaltyPlan>) : WalletUiState()
}
