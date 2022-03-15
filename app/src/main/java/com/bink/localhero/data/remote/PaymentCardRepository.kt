package com.bink.localhero.data.remote

import com.bink.localhero.model.payment_account.PaymentAccount
import com.bink.localhero.model.payment_account.SpreedlyPaymentCard
import com.bink.localhero.model.payment_account.SpreedlyResponse
import okhttp3.ResponseBody

class PaymentCardRepository(
    private val apiService: ApiService,
    private val spreedlyService: SpreedlyService
) {

    suspend fun addPaymentCard(paymentAccount: PaymentAccount): ResponseBody {
        return apiService.addPaymentCard(paymentAccount)
    }

    suspend fun sendPaymentCardToSpreedly(
        spreedlyCard: SpreedlyPaymentCard,
        environmentKey: String
    ): SpreedlyResponse {
        return spreedlyService.postPaymentCardToSpreedly(spreedlyCard, environmentKey)
    }

}