package com.bink.localhero.model.loyalty_plan

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "paymentcard")
data class PaymentCard(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "card_number") var cardNumber: String?,
) {
    constructor() : this(
        "",
        null,
    )
}