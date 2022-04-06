package com.bink.localhero.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWallet(
    @Json(name = "joins")
    val joins: List<Join>,
    @Json(name = "loyalty_cards")
    val loyaltyCards: List<LoyaltyCard>,
    @Json(name = "payment_accounts")
    val paymentAccounts: List<PaymentCard>
)

fun UserWallet.asList() : List<Any>{
    return ArrayList<Any>().apply {
        addAll(joins)
        addAll(loyaltyCards)
        addAll(paymentAccounts)
    }
}