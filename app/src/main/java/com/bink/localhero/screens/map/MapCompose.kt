package com.bink.localhero.screens.map

import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentMapComposeBinding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapCompose : BaseFragment<MapViewModel, FragmentMapComposeBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentMapComposeBinding
        get() = FragmentMapComposeBinding::inflate

    override val viewModel: MapViewModel by viewModel()

    override fun setup() {
        binding.compose.setContent {
            mapView()
        }
    }

    @Composable
    fun mapView() {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Surface(color = MaterialTheme.colors.background) {
            mapView()
        }
    }

}