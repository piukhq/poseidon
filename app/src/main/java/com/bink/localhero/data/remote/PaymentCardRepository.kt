package com.bink.localhero.data.remote

import com.bink.localhero.model.payment_account.SpreedlyPaymentCard
import com.bink.localhero.model.payment_account.SpreedlyResponse

class PaymentCardRepository(apiService: ApiService, private val spreedlyService: SpreedlyService) {

    suspend fun sendPaymentCardToSpreedly(spreedlyCard: SpreedlyPaymentCard, environmentKey: String) : SpreedlyResponse{
        return spreedlyService.postPaymentCardToSpreedly(spreedlyCard, environmentKey)
    }

}