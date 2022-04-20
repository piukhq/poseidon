package com.bink.localhero.screens.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bink.localhero.base.MainApplication
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.utils.MapUiState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

class MapViewModel(app: Application) : AndroidViewModel(app) {

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

}