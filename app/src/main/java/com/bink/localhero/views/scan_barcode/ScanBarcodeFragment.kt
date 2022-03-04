package com.bink.localhero.views.scan_barcode

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.LocalHeroFragment
import com.bink.localhero.databinding.FragmentScanBarcodeBinding
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanBarcodeFragment : LocalHeroFragment<ScanBarcodeViewModel, FragmentScanBarcodeBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentScanBarcodeBinding
        get() = FragmentScanBarcodeBinding::inflate

    override val viewModel: ScanBarcodeViewModel by viewModel()

    override fun setup() {
        launchScanner()
    }

    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()

            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission")
                Toast.makeText(
                    requireContext(),
                    "Cancelled due to missing camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d("MainActivity", "Scanned")
            Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun launchScanner() {
        val scanOption = ScanOptions()
        scanOption.setOrientationLocked(false)
        scanLauncher.launch(scanOption)
    }


}