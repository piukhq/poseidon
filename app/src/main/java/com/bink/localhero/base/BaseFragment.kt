package com.bink.localhero.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.bink.localhero.R

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    abstract val viewModel: VM

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(layoutInflater)
        setup()
        observeViewModel()
        return binding.root
    }

    abstract fun setup()

    open fun observeViewModel() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun toggleProgressDialog(){}

}