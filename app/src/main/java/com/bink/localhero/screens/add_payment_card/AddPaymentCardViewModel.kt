package com.bink.localhero.screens.add_payment_card

import androidx.lifecycle.viewModelScope
import com.bink.localhero.base.LocalHeroViewModel
import com.bink.localhero.data.remote.PaymentCardRepository
import com.bink.localhero.model.payment_account.PaymentAccount
import com.bink.localhero.model.payment_account.SpreedlyCreditCard
import com.bink.localhero.model.payment_account.SpreedlyPaymentCard
import com.bink.localhero.model.payment_account.SpreedlyPaymentMethod
import com.bink.localhero.utils.Keys
import kotlinx.coroutines.launch

class AddPaymentCardViewModel(private val addPaymentCardRepository: PaymentCardRepository) :
    LocalHeroViewModel() {

    fun sendPaymentCardToSpreedly(cardNumber: String, paymentAccount: PaymentAccount) {
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
                }
            } catch (e: Exception) {

            }
        }
    }


}