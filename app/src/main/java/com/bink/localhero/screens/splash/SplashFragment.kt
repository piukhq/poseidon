package com.bink.localhero.screens.splash

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentSplashBinding
import com.bink.localhero.utils.LocalStoreUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    override val viewModel: SplashViewModel by viewModel()

    override fun setup() {
        if (LocalStoreUtils.getAppSharedPref(LocalStoreUtils.KEY_TOKEN) != null) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToWallet())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }
}