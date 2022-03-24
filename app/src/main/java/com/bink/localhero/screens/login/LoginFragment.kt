package com.bink.localhero.screens.login

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun setup() {
        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToScanBarcodeFragment())
        }
        binding.btnAddPaymentCard.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAddPaymentCardFragment())
        }
        binding.btnGoToWallet.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWallet())
        }
    }

}