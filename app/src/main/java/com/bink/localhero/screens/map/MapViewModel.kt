package com.bink.localhero.screens.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bink.localhero.model.bakery.Bakeries
import com.bink.localhero.utils.MapUiState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.InputStream

class MapViewModel : ViewModel() {

    val londonLatLng = LatLng(51.54, -0.14)
    lateinit var map: GoogleMap

    private val _mapUiState = MutableLiveData<MapUiState>()
    val mapUiState: LiveData<MapUiState>
        get() = _mapUiState

    fun getBakeries(inputStream: InputStream) {
        val byteArray = ByteArray(inputStream.available())
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(Bakeries::class.java)

        inputStream.read(byteArray, 0, byteArray.size)
        adapter.fromJson(String(byteArray))?.let { bakeries ->
            _mapUiState.value = MapUiState.ShowBakeries(bakeries)
        }
        inputStream.close()
    }

}