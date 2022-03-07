package com.bink.localhero.screens.wallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bink.localhero.R
import com.bink.localhero.databinding.WalletFragmentBinding
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.screens.wallet.adapter.WalletAdapter
import com.bink.localhero.utils.ui_state.WalletUiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : Fragment() {

    private val viewModel: WalletViewModel by viewModel()
    private var _binding: WalletFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = WalletFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        walletAdapter = WalletAdapter(mutableListOf())
        setupRecyclerView()
        viewModel.getPlans()

        viewModel.walletUiState.observe(viewLifecycleOwner, {
            when (it) {
                is WalletUiState.Loading -> showProgress()
                is WalletUiState.Error -> showError(it.message)
                is WalletUiState.Success -> showPlans(it.plans)
            }
        })
    }

    private fun showPlans(plans: List<LoyaltyPlan>) {
        walletAdapter.setData(plans)
        Log.d("Wallet", plans.size.toString())
    }

    private fun showError(message: String?) {
        Log.d("Wallet", message!!)

    }

    private fun showProgress() {

    }

    private fun setupRecyclerView() {
        with(binding) {
            rvPlansList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = walletAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}