package com.bink.localhero.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import com.bink.localhero.BuildConfig
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.model.bakery.Properties
import com.bink.localhero.utils.MapUiState
import com.bink.localhero.utils.showDialog
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object {
        const val LOCATION_CODE = 1
    }

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        viewModel.fusedLocationProvider =
            LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
    }

    override fun observeViewModel() {
        viewModel.mapUiState.observe(viewLifecycleOwner) {
            when (it) {
                is MapUiState.ShowBakeries -> displayBakeries(it.bakeries)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            viewModel.requestLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (hasLocationPermission()) {
            viewModel.removeLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_CODE -> {
                setMapCurrentLocationSettings()
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasLocationPermission()) {
                        viewModel.requestLocationUpdates()
                    }
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.map = googleMap
        viewModel.getBakeries()
        setMapCurrentLocationSettings()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val properties = marker.tag as Properties
        requireContext().showDialog(
            title = properties.locationName,
            message = "Address: ${properties.streetAddress}\nOpening Hours: ${
                properties.openHours.replace(
                    "\\",
                    ""
                )
            }",
            cancelable = true
        )
        return true
    }

    private fun displayBakeries(bakeries: Bakeries) {
        val map = viewModel.map

        bakeries.features.forEach {
            val bakeryLatLng = LatLng(it.geometry.coordinates[1], it.geometry.coordinates[0])
            val marker = map.addMarker(MarkerOptions().position(bakeryLatLng))
            marker?.tag = it.properties
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.londonLatLng, 8f))
        map.setOnMarkerClickListener(this)
    }

    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requireContext().showDialog(
                    title = getString(R.string.location_permission_title),
                    message = getString(R.string.location_permission_message),
                    positiveBtn = getString(R.string.okay),
                    negativeBtn = getString(R.string.cancel),
                    positiveCallback = { requestLocationPermission() })
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            LOCATION_CODE
        )
    }

    private fun setMapCurrentLocationSettings() {
        if (!hasLocationPermission()) {
            viewModel.map.isMyLocationEnabled = false
            viewModel.map.uiSettings.isMyLocationButtonEnabled = false
        } else {
            viewModel.map.isMyLocationEnabled = true
            viewModel.map.uiSettings.isMyLocationButtonEnabled = true
        }
    }


}