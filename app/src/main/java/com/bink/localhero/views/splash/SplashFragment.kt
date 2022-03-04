package com.bink.localhero.views.splash

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.LocalHeroFragment
import com.bink.localhero.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : LocalHeroFragment<SplashViewModel, FragmentSplashBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    override val viewModel: SplashViewModel by viewModel()

    override fun setup() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
    }
}