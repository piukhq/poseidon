package com.bink.localhero.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bink.localhero.model.loyalty_plan.PaymentCard

@Database(
    entities = [PaymentCard::class],
    version = 1
)
abstract class LocalHeroDatabase : RoomDatabase() {
    abstract fun paymentCardDao(): PaymentCardDao
}