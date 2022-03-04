package com.bink.localhero.di

import androidx.room.Room
import com.bink.localhero.data.local.LocalHeroDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModules = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            LocalHeroDatabase::class.java, "localhero-db"
        ).build()
    }

    single { get<LocalHeroDatabase>().paymentCardDao() }
}