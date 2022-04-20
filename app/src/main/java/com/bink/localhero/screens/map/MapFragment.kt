package com.bink.localhero.screens.map

import android.view.LayoutInflater
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.utils.MapUiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>(), OnMapReadyCallback {

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun observeViewModel() {
        viewModel.mapUiState.observe(viewLifecycleOwner) {
            when (it) {
                is MapUiState.ShowBakeries -> displayBakeries(it.bakeries)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.map = googleMap
        viewModel.getBakeries(requireContext().assets.open("bakeries.json"))
    }

    private fun displayBakeries(bakeries: Bakeries) {
        val map = viewModel.map
        bakeries.features.forEach {
            val bakeryLatLng = LatLng(it.geometry.coordinates[1], it.geometry.coordinates[0])
            map.addMarker(
                MarkerOptions()
                    .position(bakeryLatLng)
                    .title(it.properties.locationName)
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.londonLatLng, 8f))
        }
    }

}