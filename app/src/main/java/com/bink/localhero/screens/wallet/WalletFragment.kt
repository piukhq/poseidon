package com.bink.localhero.screens.wallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bink.localhero.databinding.WalletFragmentBinding
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan
import com.bink.localhero.screens.wallet.adapter.WalletAdapter
import com.bink.localhero.utils.ui_state.WalletUiState
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.lang.Exception

class WalletFragment : Fragment() {

    private val viewModel: WalletViewModel by viewModel()
    private var _binding: WalletFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WalletFragmentBinding.inflate(inflater, container, false)
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
                is WalletUiState.Error -> showError(it.exception)
                is WalletUiState.Success -> showPlans(it.plans)
            }
        })

        binding.imgMore.setOnClickListener {
            findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToModalBottomSheet())
        }
    }

    private fun showPlans(plans: List<LoyaltyPlan>) {
        walletAdapter.setData(plans)
        Log.d("Wallet", plans.size.toString())
    }

    private fun showError(exception: Exception?) {
        val httpException = exception as HttpException
        if (httpException.code() == 401) {
            showAlertDialog("It appears that your token is invalid")
        } else {
            showAlertDialog("Unexpected error happened")
        }
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

    private fun showAlertDialog(message:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Something went wrong")
            setMessage(message)
            setPositiveButton("Try again ?") { _, _ ->
                findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToLoginFragment())

            }
            setCancelable(false)
            create()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}