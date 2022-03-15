package com.bink.localhero.screens.add_payment_card

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.localhero.data.remote.PaymentCardRepository
import com.bink.localhero.model.payment_account.PaymentAccount
import com.bink.localhero.model.payment_account.SpreedlyCreditCard
import com.bink.localhero.model.payment_account.SpreedlyPaymentCard
import com.bink.localhero.model.payment_account.SpreedlyPaymentMethod
import com.bink.localhero.utils.AddPaymentCardUiState
import com.bink.localhero.utils.Keys
import com.bink.localhero.utils.WalletUiState
import kotlinx.coroutines.launch

class AddPaymentCardViewModel(private val addPaymentCardRepository: PaymentCardRepository) :
    ViewModel() {

    private val _addPaymentCardUiState = MutableLiveData<AddPaymentCardUiState>()
    val addPaymentCardUiState: LiveData<AddPaymentCardUiState>
        get() = _addPaymentCardUiState

    fun sendPaymentCardToSpreedly(cardNumber: String, paymentAccount: PaymentAccount) {

        _addPaymentCardUiState.value = AddPaymentCardUiState.Loading

        val spreedlyCreditCard = SpreedlyCreditCard(
            cardNumber,
            paymentAccount.expiryMonth,
            paymentAccount.expiryYear,
            paymentAccount.nameOnCard
        )

        val spreedlyPaymentMethod = SpreedlyPaymentMethod(spreedlyCreditCard, "true")
        val spreedlyPaymentCard = SpreedlyPaymentCard(spreedlyPaymentMethod)

        viewModelScope.launch {
            try {
                val spreedlyResponse = addPaymentCardRepository.sendPaymentCardToSpreedly(
                    spreedlyPaymentCard,
                    Keys.spreedlyKey()
                )

                spreedlyResponse.let { response ->
                    paymentAccount.apply {
                        token = response.transaction.payment_method.token
                        fingerprint = response.transaction.payment_method.fingerprint
                        firstSixDigits = response.transaction.payment_method.first_six_digits
                        lastFourDigits = response.transaction.payment_method.last_four_digits
                    }

                    addPaymentCardRepository.addPaymentCard(paymentAccount)
                    _addPaymentCardUiState.value = AddPaymentCardUiState.Success
                }
            } catch (e: Exception) {
                _addPaymentCardUiState.value = AddPaymentCardUiState.Error(e)
            }
        }
    }


}