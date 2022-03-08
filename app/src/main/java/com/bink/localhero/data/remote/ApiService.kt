package com.bink.localhero.data.remote

import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.model.payment_account.PaymentAccount
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/loyalty_plans")
    suspend fun getLoyaltyPlans():List<LoyaltyPlan>

    @POST("/paymentAccounts")
    suspend fun addPaymentCard(paymentAccount: PaymentAccount)
}