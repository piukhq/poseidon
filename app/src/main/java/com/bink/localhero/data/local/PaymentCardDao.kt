package com.bink.localhero.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bink.localhero.model.PaymentCard

@Dao
interface PaymentCardDao {
    @Query("SELECT * FROM paymentcard")
    suspend fun getPaymentCard(): PaymentCard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePaymentCard(paymentCard: PaymentCard)

    @Query("DELETE FROM paymentcard")
    suspend fun deletePaymentCard()
}