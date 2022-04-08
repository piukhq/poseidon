package com.bink.localhero.di

import com.bink.localhero.data.remote.ApiService
import com.bink.localhero.data.remote.PaymentCardRepository
import com.bink.localhero.data.remote.SpreedlyService
import com.bink.localhero.screens.add_payment_card.AddPaymentCardViewModel
import com.bink.localhero.screens.wallet.WalletRepository
import com.bink.localhero.screens.wallet.WalletViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        provideWalletRepository(get())
    }

    single {
        providePaymentCardRepository(get(), get())
    }

    viewModel { WalletViewModel(get()) }
    viewModel { AddPaymentCardViewModel(get()) }
}

fun provideWalletRepository(apiService: ApiService): WalletRepository = WalletRepository(apiService)

fun providePaymentCardRepository(
    apiService: ApiService,
    spreedlyService: SpreedlyService
): PaymentCardRepository = PaymentCardRepository(apiService, spreedlyService)

