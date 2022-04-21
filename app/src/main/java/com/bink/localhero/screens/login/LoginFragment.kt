package com.bink.localhero.screens.login

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentLoginBinding
import com.bink.localhero.utils.LocalStoreUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun setup() {
        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToScanBarcodeFragment())
        }

        binding.btnProcessToken.setOnClickListener {
            if (binding.etTokenValue.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Enter a token before proceding",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                LocalStoreUtils.setAppSharedPref(
                    LocalStoreUtils.KEY_TOKEN,
                    binding.etTokenValue.text.toString()
                )
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToScanBarcodeFragment())

            }
        }
    }

}