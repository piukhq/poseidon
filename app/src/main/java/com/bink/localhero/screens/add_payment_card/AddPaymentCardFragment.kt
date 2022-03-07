package com.bink.localhero.screens.add_payment_card

import android.view.LayoutInflater
import com.bink.localhero.base.LocalHeroFragment
import com.bink.localhero.databinding.FragmentAddPaymentCardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPaymentCardFragment :
    LocalHeroFragment<AddPaymentCardViewModel, FragmentAddPaymentCardBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentAddPaymentCardBinding
        get() = FragmentAddPaymentCardBinding::inflate

    override val viewModel: AddPaymentCardViewModel by viewModel()

    override fun setup() {

    }
}