package com.bink.localhero.screens.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bink.localhero.databinding.BottomSheetModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetModalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAddPaymentCard.setOnClickListener {
            findNavController().navigate(ModalBottomSheetDirections.actionModalBottomSheetToAddPaymentCardFragment())
        }

        binding.tvLocations.setOnClickListener {
            findNavController().navigate(ModalBottomSheetDirections.actionModalBottomSheetToMap())
        }

        binding.tvSettings.setOnClickListener {
            findNavController().navigate(ModalBottomSheetDirections.actionModalBottomSheetToSettings())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}