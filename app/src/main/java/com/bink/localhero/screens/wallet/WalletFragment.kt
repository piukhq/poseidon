package com.bink.localhero.screens.wallet

import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.WalletFragmentBinding
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.model.wallet.LoyaltyCard
import com.bink.localhero.model.wallet.PaymentCard
import com.bink.localhero.model.wallet.UserWallet
import com.bink.localhero.screens.wallet.adapter.PlansAdapter
import com.bink.localhero.screens.wallet.adapter.UserWalletAdapter
import com.bink.localhero.utils.WalletUiState
import com.bink.localhero.utils.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException

class WalletFragment : BaseFragment<WalletViewModel, WalletFragmentBinding>() {

    private lateinit var plansAdapter: PlansAdapter
    private lateinit var walletAdapter: UserWalletAdapter

    override val bindingInflater: (LayoutInflater) -> WalletFragmentBinding
        get() = WalletFragmentBinding::inflate

    override val viewModel: WalletViewModel by viewModel()

    override fun setup() {
        plansAdapter = PlansAdapter(mutableListOf())
        walletAdapter = UserWalletAdapter(onClickListener = {
            onCardClick(it)
        })
        setupRecyclerView()
        //viewModel.getPlans()
        viewModel.getWallet()

        binding.imgMore.setOnClickListener {
            findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToModalBottomSheet())
        }
    }

    override fun observeViewModel() {
        viewModel.walletUiState.observe(viewLifecycleOwner) {
            when (it) {
                is WalletUiState.Loading -> toggleProgressDialog()
                is WalletUiState.Error -> showError(it.exception)
                is WalletUiState.ShowPlans -> showPlans(it.plans)
                is WalletUiState.ShowWallet -> showWallet(it.wallet)
            }
        }
    }

    private fun showPlans(plans: List<LoyaltyPlan>) {
        plansAdapter.setData(plans)
        Log.d("Wallet", plans.size.toString())
    }

    private fun showWallet(wallet: UserWallet) {
        walletAdapter.setData(wallet)
    }

    private fun onCardClick(item: Any) {
        when (item) {
            is PaymentCard -> {
                requireContext().showDialog(
                    title = "Payment Card Details",
                    message = "Id: ${item.id}" +
                            "\nName: ${item.nameOnCard}" +
                            "\nNickname: ${item.cardNickname}" +
                            "\nLast Four: ${item.lastFourDigits}" +
                            "\nExpiry: ${item.expiryMonth}/${item.expiryYear}" +
                            "\nStatus: ${item.status}" +
                            "\nHas Links: ${item.pllLinks?.isNotEmpty()}",
                    positiveBtn = "Done"
                )
            }
            is LoyaltyCard -> {
                requireContext().showDialog(
                    title = "Loyalty Card Details",
                    message = "Id: ${item.id}" +
                            "\nPlan Name: ${item.loyaltyPlanName}" +
                            "\nBalance: ${item.balance?.currentDisplayValue}" +
                            "\nPlan Id: ${item.loyaltyPlanId}" +
                            "\nHas Transactions: ${item.transactions?.isNotEmpty()}" +
                            "\nStatus: ${item.status?.description}" +
                            "\nHas Vouchers: ${item.vouchers?.isNotEmpty()}",
                    positiveBtn = "Done"
                )
            }
        }
    }

    private fun showError(exception: Exception?) {
        val httpException = exception as? HttpException

        if (httpException?.code() ?: 0 == 401) {
            requireContext().showDialog(
                title = getString(R.string.error_title),
                message = getString(R.string.login_invalid_token),
                positiveBtn = getString(R.string.try_again),
                negativeBtn = getString(R.string.cancel),
                positiveCallback = { viewModel.getPlans() },
                negativeCallback = { findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToLoginFragment()) })
        } else {
            requireContext().showDialog(
                title = getString(R.string.error_title),
                message = getString(R.string.unexpected_error),
                positiveBtn = getString(R.string.try_again),
                negativeBtn = getString(R.string.cancel),
                positiveCallback = { viewModel.getPlans() },
                negativeCallback = { findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToLoginFragment()) })
        }

    }

    private fun setupRecyclerView() {
        with(binding) {
//            rvPlansList.apply {
//                layoutManager = LinearLayoutManager(context)
//                adapter = plansAdapter
//            }

            rvPlansList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = walletAdapter
            }
        }

    }
}