package com.bink.localhero.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import com.bink.localhero.model.bakery.Properties
import com.bink.localhero.utils.showDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>() {

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 100
        private const val LOCATION_REQUEST_INTERVAL: Long = 5000
    }

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val mapProperties = mutableStateOf(MapProperties(isMyLocationEnabled = false))
    private val uiSettings = mutableStateOf(MapUiSettings(myLocationButtonEnabled = false))

    //Defaulted to London
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

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    manageLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    manageLocation()
                }
            }

            setMapCurrentLocationSettings()
        }

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {
        viewModel.getBakeries()

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()

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

    private fun turnOnLocation() {
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create().apply {
                interval = LOCATION_REQUEST_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            })
        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
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
        } else {
            requestLocationPermission()
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
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun setMapCurrentLocationSettings() {
        if (hasLocationPermission()) {
            mapProperties.value = MapProperties(isMyLocationEnabled = true)
            uiSettings.value = MapUiSettings(myLocationButtonEnabled = true)
            getLocation()
        } else {
            mapProperties.value = MapProperties(isMyLocationEnabled = false)
            uiSettings.value = MapUiSettings(myLocationButtonEnabled = false)
        }
    }

    private fun manageLocation() {
        if (isLocationTurnedOn()) {
            getLocation()
        } else {
            turnOnLocation()
        }
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


    private fun isLocationTurnedOn(): Boolean =
        (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
}