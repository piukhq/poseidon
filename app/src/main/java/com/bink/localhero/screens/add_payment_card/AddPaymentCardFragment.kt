package com.bink.localhero.screens.add_payment_card

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentAddPaymentCardBinding
import com.bink.localhero.model.payment_account.PaymentAccount
import com.bink.localhero.screens.wallet.WalletFragmentDirections
import com.bink.localhero.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPaymentCardFragment :
    BaseFragment<AddPaymentCardViewModel, FragmentAddPaymentCardBinding>() {

    companion object {
        const val EXPIRY_YEAR = 2000
    }

    override val bindingInflater: (LayoutInflater) -> FragmentAddPaymentCardBinding
        get() = FragmentAddPaymentCardBinding::inflate

    override val viewModel: AddPaymentCardViewModel by viewModel()

    override fun setup() {
        binding.etCardNumber.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                if (binding.etCardNumber.text.toString().cardValidation() == PaymentCardType.NONE) {
                    binding.tilCardNumber.error = "Invalid card number"
                } else {
                    binding.tilCardNumber.error = null
                }
            } else {
                binding.tilCardNumber.error = null
            }
        }

        binding.etCardNumber.addTextChangedListener {
            it?.trim().toString().let { cardNumber ->
                cardNumber.presentedCardType().apply {
                    binding.tvPaymentCardType.text = this.name
                }
            }
            checkAddBtnEnable()
        }

        binding.etNameOnCard.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                if (binding.etNameOnCard.text.toString().isNullOrEmpty()) {
                    binding.tilNameOnCard.error = "Invalid name"
                } else {
                    binding.tilNameOnCard.error = null
                }
            } else {
                binding.tilNameOnCard.error = null
            }
        }

        binding.etNameOnCard.addTextChangedListener {
            checkAddBtnEnable()
        }

        binding.etExpiry.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.etExpiry.text.toString().let {
                    if (!it.dateValidation()) {
                        binding.tilExpiry.error = "Invalid expiry date"
                    } else {
                        binding.tilExpiry.error = null
                    }

                    if (!it.formatDate().contentEquals(it)) {
                        binding.etExpiry.setText(it.formatDate())
                    }
                }

            } else {
                binding.tilExpiry.error = null
            }
        }

        binding.etExpiry.addTextChangedListener {
            checkAddBtnEnable()
        }

        binding.btnAddPaymentCard.setOnClickListener {
            postCard()
        }
    }

    override fun observeViewModel() {
        viewModel.addPaymentCardUiState.observe(viewLifecycleOwner) {
            when (it) {
                is AddPaymentCardUiState.Loading -> toggleProgressDialog()
                is AddPaymentCardUiState.Error -> showError(it.exception)
                is AddPaymentCardUiState.Success -> goToWallet()
            }
        }
    }

    private fun showError(exception: Exception?) {
        requireContext().showDialog(title = getString(R.string.error_title),
            message = exception?.localizedMessage,
            positiveBtn = getString(R.string.try_again),
            negativeBtn = getString(R.string.cancel),
            positiveCallback = { postCard() },
            negativeCallback = {})
    }

    private fun goToWallet() {
        findNavController().navigate(AddPaymentCardFragmentDirections.actionAddPaymentCardToWallet())
    }

    private fun checkAddBtnEnable() {
        binding.btnAddPaymentCard.isEnabled =
            ((binding.tilCardNumber.error == null && binding.etCardNumber.text.toString()
                .cardValidation() != PaymentCardType.NONE)
                    && (binding.tilNameOnCard.error == null && binding.etNameOnCard.text.toString()
                .isNotEmpty())
                    && (binding.tilExpiry.error == null && binding.etExpiry.text.toString()
                .dateValidation()))
    }

    private fun getPaymentAccount(): PaymentAccount {
        val nameOnCard = binding.etNameOnCard.text.toString()
        val nickname = binding.etNickname.text.toString()
        val cardNumber = binding.etCardNumber.text.toString()
        val cardExpiry = binding.etExpiry.text.toString().split("/")

        return PaymentAccount(
            cardNickname = nickname,
            country = "GB",
            currencyCode = "GBP",
            expiryMonth = cardExpiry[0],
            expiryYear = (cardExpiry[1].toInt() + EXPIRY_YEAR).toString(),
            fingerprint = PaymentAccount.fingerprintGenerator(
                cardNumber,
                cardExpiry[0],
                cardExpiry[1]
            ),
            firstSixDigits = cardNumber.substring(0, 6),
            lastFourDigits = cardNumber.substring(cardNumber.length - 4),
            nameOnCard = nameOnCard,
            token = PaymentAccount.tokenGenerator()
        )

    }

    private fun postCard() {
        viewModel.sendPaymentCardToSpreedly(
            binding.etCardNumber.text.toString(),
            getPaymentAccount()
        )
        Toast.makeText(requireContext(), "Post Card to Spreedly", Toast.LENGTH_LONG).show()
    }


}