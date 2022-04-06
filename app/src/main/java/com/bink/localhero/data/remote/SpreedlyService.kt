package com.bink.localhero.data.remote

import com.bink.localhero.model.payment_account.SpreedlyPaymentCard
import com.bink.localhero.model.payment_account.SpreedlyResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SpreedlyService {

    @Headers("Content-Type: application/json")
    @POST("https://core.spreedly.com/v1/payment_methods.json")
    suspend fun postPaymentCardToSpreedly(
        @Body spreedlyCard: SpreedlyPaymentCard,
        @Query("environment_key") environmentKey: String
    ): SpreedlyResponse

}