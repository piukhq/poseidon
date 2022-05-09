package com.bink.localhero.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bink.localhero.BuildConfig
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.model.bakery.Properties
import com.bink.localhero.utils.MapUiState
import com.bink.localhero.utils.showDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import org.koin.androidx.viewmodel.ext.android.viewModel


@SuppressLint("MissingPermission")
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private lateinit var map: GoogleMap

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        setMapCurrentLocationSettings()
        if (isGranted) {
            getLocation()
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requireContext().showDialog(
                    title = getString(R.string.location_permission_settings_title),
                    message = getString(R.string.location_permission_settings_message),
                    positiveBtn = getString(R.string.okay),
                    negativeBtn = getString(R.string.cancel),
                    positiveCallback = {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null),
                            ),
                        )
                    })
            }
        }
    }

    override fun setup() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
    }

    override fun observeViewModel() {
        viewModel.mapUiState.observe(viewLifecycleOwner) {
            when (it) {
                is MapUiState.ShowBakeries -> displayBakeries(it.bakeries)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
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
        bakeries.features.forEach {
            val bakeryLatLng = LatLng(it.geometry.coordinates[1], it.geometry.coordinates[0])
            val marker = map.addMarker(MarkerOptions().position(bakeryLatLng))
            marker?.tag = it.properties
        }

       // map.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.londonLatLng, 8f))
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

    private fun getLocation() {
        fusedLocationProvider?.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )?.addOnSuccessListener { location ->
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), 10f
                )
            )
        }
    }

    private fun requestLocationPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun setMapCurrentLocationSettings() {
        if (!hasLocationPermission()) {
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
        } else {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            getLocation()
        }
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


}