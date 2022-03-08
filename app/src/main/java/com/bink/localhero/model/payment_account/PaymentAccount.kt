package com.bink.localhero.model.payment_account


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentAccount(
    @Json(name = "card_nickname")
    val cardNickname: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "currency_code")
    val currencyCode: String,
    @Json(name = "expiry_month")
    val expiryMonth: String,
    @Json(name = "expiry_year")
    val expiryYear: String,
    @Json(name = "fingerprint")
    var fingerprint: String,
    @Json(name = "first_six_digits")
    var firstSixDigits: String,
    @Json(name = "issuer")
    val issuer: String,
    @Json(name = "last_four_digits")
    var lastFourDigits: String,
    @Json(name = "name_on_card")
    val nameOnCard: String,
    @Json(name = "provider")
    val provider: String,
    @Json(name = "token")
    var token: String,
    @Json(name = "type")
    val type: String
)