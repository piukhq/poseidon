package com.bink.localhero.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bink.localhero.BuildConfig
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapComposeBinding
import com.bink.localhero.model.bakery.Properties
import com.bink.localhero.utils.showDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MapCompose : BaseFragment<MapViewModel, FragmentMapComposeBinding>() {

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val mapProperties = mutableStateOf(MapProperties(isMyLocationEnabled = false))
    private val uiSettings = mutableStateOf(MapUiSettings(myLocationButtonEnabled = false))
    private val cameraPositionState = mutableStateOf(
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                LatLng(
                    51.54,
                    -0.14
                ), 10f
            )
        )
    )

    override val bindingInflater: (LayoutInflater) -> FragmentMapComposeBinding
        get() = FragmentMapComposeBinding::inflate

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
        viewModel.getBakeries()
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
        setMapCurrentLocationSettings()

        binding.compose.setContent {
            MapView()
        }
    }

    @Composable
    fun MapView() {
        val markerClick: (Marker) -> Boolean = {
            val properties = it.tag as Properties
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
            true
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState.value,
            uiSettings = uiSettings.value,
            properties = mapProperties.value
        ) {

            viewModel.mutableBakeries.value?.features?.forEach {
                val bakeryLatLng = LatLng(it.geometry.coordinates[1], it.geometry.coordinates[0])
                Marker(
                    state = MarkerState(position = bakeryLatLng),
                    tag = it.properties,
                    onClick = markerClick
                )
            }

        }
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

    private fun setMapCurrentLocationSettings() {
        if (!hasLocationPermission()) {
            mapProperties.value = MapProperties(isMyLocationEnabled = false)
            uiSettings.value = MapUiSettings(myLocationButtonEnabled = false)
        } else {
            mapProperties.value = MapProperties(isMyLocationEnabled = true)
            uiSettings.value = MapUiSettings(myLocationButtonEnabled = true)
            getLocation()
        }
    }

    private fun getLocation() {
        fusedLocationProvider?.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )?.addOnSuccessListener { location ->
            cameraPositionState.value = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
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

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


}