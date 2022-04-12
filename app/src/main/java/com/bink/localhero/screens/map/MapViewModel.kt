package com.bink.localhero.screens.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.utils.MapUiState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.io.InputStream

class MapViewModel : ViewModel() {

    val londonLatLng = LatLng(51.54, -0.14)
    lateinit var map: GoogleMap

    private val _mapUiState = MutableLiveData<MapUiState>()
    val mapUiState: LiveData<MapUiState>
        get() = _mapUiState

    fun getBakeries(inputStream: InputStream) {
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes, 0, bytes.size)
        val string = String(bytes)
        val json = Gson().fromJson(string, Bakeries::class.java)
        _mapUiState.value =
            MapUiState.ShowBakeries(json)
    }

}