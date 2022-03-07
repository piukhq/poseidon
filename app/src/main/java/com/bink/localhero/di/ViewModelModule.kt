package com.bink.localhero.di

import com.bink.localhero.network.ApiService
import com.bink.localhero.screens.wallet.WalletRepository
import com.bink.localhero.screens.wallet.WalletViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        provideWalletRepository(get())
    }

    viewModel { WalletViewModel(get()) }
}

fun provideWalletRepository(apiService: ApiService): WalletRepository = WalletRepository(apiService)

