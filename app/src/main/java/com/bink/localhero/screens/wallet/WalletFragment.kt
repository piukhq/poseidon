package com.bink.localhero.screens.wallet

import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.WalletFragmentBinding
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.screens.wallet.adapter.WalletAdapter
import com.bink.localhero.utils.WalletUiState
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException

class WalletFragment : BaseFragment<WalletViewModel, WalletFragmentBinding>() {

    private lateinit var walletAdapter: WalletAdapter

    override val bindingInflater: (LayoutInflater) -> WalletFragmentBinding
        get() = WalletFragmentBinding::inflate

    override val viewModel: WalletViewModel by viewModel()

    override fun setup() {
        walletAdapter = WalletAdapter(mutableListOf())
        setupRecyclerView()
        viewModel.getPlans()
    }

    override fun observeViewModel() {
        viewModel.walletUiState.observe(viewLifecycleOwner) {
            when (it) {
                is WalletUiState.Loading -> toggleProgressDialog()
                is WalletUiState.Error -> showError(it.exception)
                is WalletUiState.Success -> showPlans(it.plans)
            }
        }
    }

    private fun showPlans(plans: List<LoyaltyPlan>) {
        walletAdapter.setData(plans)
        Log.d("Wallet", plans.size.toString())
    }


    private fun showError(exception: Exception?) {
        val httpException = exception as HttpException

        if (httpException.code() == 401) {
            showDialog(getString(R.string.error_title),
                getString(R.string.login_invalid_token),
                getString(R.string.try_again),
                getString(R.string.cancel),
                { viewModel.getPlans() },
                { findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToLoginFragment()) })
        }

    }

    private fun setupRecyclerView() {
        with(binding) {
            rvPlansList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = walletAdapter
            }
        }
    }
}