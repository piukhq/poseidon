package com.bink.localhero.screens.map

import android.view.LayoutInflater
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {

    }

    override fun observeViewModel() {

    }

}