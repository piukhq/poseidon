package com.bink.localhero.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bink.localhero.R
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapBinding
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.model.bakery.Properties
import com.bink.localhero.utils.MapUiState
import com.bink.localhero.utils.showDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private lateinit var map: GoogleMap
    private lateinit var locationRequest: LocationRequest

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 100
    }

    override val bindingInflater: (LayoutInflater) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        when {
            permissions.get(Manifest.permission.ACCESS_FINE_LOCATION) ?: false -> {
                // Precise location access granted.
                manageLocation()
            }
            permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION) ?: false -> {
                // Only approximate location access granted.
                manageLocation()
            }
            else -> {
                // No location access granted.
                Log.d("MapFragment", "No location permission granted")
            }
        }

        setMapCurrentLocationSettings()
    }

    /**
     * We'll use this these methods to receive location updates
     * Need to clarify use case oas this might not be needed
     */
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }
    }

    override fun setup() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = 6000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        checkLocationPermission()
    }

    /**
     * This method shows the dialog to the user to turn on location
     * Removing the need to go to settings
     */
    private fun turnOnLocation() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())

        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
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
        } else {
            requestLocationPermission()

        }
    }

    private fun getLocation() {
        fusedLocationProvider?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        fusedLocationProvider?.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )?.addOnSuccessListener { location ->
            location?.let {
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


    private fun isLocationTurnedOn(): Boolean {
        // Calling Location Manager
        val mLocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Checking location is enabled
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /** This checks whether the location is turned on on the device
     *  If location is turned on, we display the user's location
     *  Otherwise, show the location dialog
     */
    private fun manageLocation() {
        if (isLocationTurnedOn()) {
            getLocation()
        } else {
            turnOnLocation()
        }
    }
}