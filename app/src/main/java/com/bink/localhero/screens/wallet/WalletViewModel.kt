package com.bink.localhero.screens.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.localhero.utils.ui_state.WalletUiState
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepository: WalletRepository) : ViewModel() {

    private val _walletUiState = MutableLiveData<WalletUiState>()
    val walletUiState: LiveData<WalletUiState>
        get() = _walletUiState

    fun getPlans() {
        _walletUiState.value = WalletUiState.Loading
        viewModelScope.launch {
            try {
                val plans = walletRepository.getPlans()
                _walletUiState.value = WalletUiState.Success(plans)
            } catch (e: Exception) {
                _walletUiState.value = WalletUiState.Error(e.message)
            }
        }
    }
}