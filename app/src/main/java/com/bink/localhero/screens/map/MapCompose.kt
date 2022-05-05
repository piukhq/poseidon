package com.bink.localhero.screens.map

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapComposeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapCompose : BaseFragment<MapViewModel, FragmentMapComposeBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentMapComposeBinding
        get() = FragmentMapComposeBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {
        binding.compose.setContent {
            mapView()
        }
    }

    @Composable
    fun mapView() {

    }
}