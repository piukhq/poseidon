package com.bink.localhero.screens.wallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bink.localhero.R
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.utils.ui_state.WalletUiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : Fragment() {

    private val viewModel : WalletViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wallet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlans()

        viewModel.walletUiState.observe(viewLifecycleOwner,{
            when(it){
                is WalletUiState.Loading -> showProgress()
                is WalletUiState.Error -> showError(it.message)
                is WalletUiState.Success -> showPlans(it.plans)
            }
        })
    }

    private fun showPlans(plans: List<LoyaltyPlan>) {
        Log.d("Wallet",plans.size.toString())
    }

    private fun showError(message: String?) {
        Log.d("Wallet",message!!)

    }

    private fun showProgress() {

    }
}