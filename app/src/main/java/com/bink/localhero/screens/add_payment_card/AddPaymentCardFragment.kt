package com.bink.localhero.screens.add_payment_card

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentAddPaymentCardBinding
import com.bink.localhero.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPaymentCardFragment :
    BaseFragment<AddPaymentCardViewModel, FragmentAddPaymentCardBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentAddPaymentCardBinding
        get() = FragmentAddPaymentCardBinding::inflate

    override val viewModel: AddPaymentCardViewModel by viewModel()

    override fun setup() {
        binding.etCardNumber.addTextChangedListener {
            it?.trim().toString().let { cardNumber ->
                cardNumber.presentedCardType().apply {
                    binding.tvPaymentCardType.text = this.name
                }

                if (cardNumber.cardValidation() == PaymentCardType.NONE) {
                    binding.tilCardNumber.error = "Invalid card number"
                } else {
                    binding.tilCardNumber.error = null
                }
            }

            checkAddBtnEnable()
        }

        binding.etExpiry.addTextChangedListener {
            binding.tilExpiry.error = cardExpiryErrorCheck(it?.trim().toString())
            checkAddBtnEnable()
        }

        binding.btnAddPaymentCard.setOnClickListener {
            Toast.makeText(requireContext(), "Post Card to Spreedly", Toast.LENGTH_LONG).show()
        }
    }

    private fun cardExpiryErrorCheck(text: String): String? {
        with(text) {
            if (!dateValidation()) {
                return "Invalid expiry date"
            }
        }
        return null
    }

    private fun checkAddBtnEnable() {
        binding.btnAddPaymentCard.isEnabled =
            ((binding.tilCardNumber.error == null && !binding.etCardNumber.text.isNullOrEmpty())
                    && (binding.tilExpiry.error == null && !binding.etExpiry.text.isNullOrEmpty()))
    }


}