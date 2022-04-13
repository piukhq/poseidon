package com.bink.localhero.screens.map

import android.annotation.SuppressLint
import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bink.localhero.base.MainApplication
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.utils.MapUiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

class MapViewModel(app: Application) : AndroidViewModel(app) {

    var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                if (isFirstUpdate) {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 10f
                        )
                    )
                    isFirstUpdate = false
                }
            }
        }
    }

    private var isFirstUpdate = true

    val londonLatLng = LatLng(51.54, -0.14)
    lateinit var map: GoogleMap

    private val _mapUiState = MutableLiveData<MapUiState>()
    val mapUiState: LiveData<MapUiState>
        get() = _mapUiState

    fun getBakeries() {
        try {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(Bakeries::class.java)
            val jsonString =
                getApplication<MainApplication>().assets.open("bakeries.json").bufferedReader()
                    .use { it.readText() }

            adapter.fromJson(jsonString)?.let { bakeries ->
                _mapUiState.value = MapUiState.ShowBakeries(bakeries)
            }
        } catch (e: IOException) {
            _mapUiState.value = MapUiState.Error
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        fusedLocationProvider?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun removeLocationUpdates(){
        fusedLocationProvider?.removeLocationUpdates(locationCallback)
    }

}