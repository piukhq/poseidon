package com.bink.localhero.screens.scanBarcode

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.databinding.FragmentScanBarcodeBinding
import com.bink.localhero.utils.LocalStoreUtils
import com.bink.localhero.utils.LocalStoreUtils.KEY_TOKEN
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanBarcodeFragment : BaseFragment<ScanBarcodeViewModel, FragmentScanBarcodeBinding>() {

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
            val token = result.contents
            Toast.makeText(requireContext(), "Scanned: $token", Toast.LENGTH_LONG)
                .show()

            LocalStoreUtils.setAppSharedPref(KEY_TOKEN, token)
            findNavController().navigate(ScanBarcodeFragmentDirections.actionScanBarcodeFragmentToWalletFragment())
        }
    }

    override val bindingInflater: (LayoutInflater) -> FragmentScanBarcodeBinding
        get() = FragmentScanBarcodeBinding::inflate

    override val viewModel: ScanBarcodeViewModel by viewModel()

    override fun setup() {

    }

    override fun onResume() {
        super.onResume()
        launchScanner()
    }

    private fun launchScanner() {
        val scanOption = ScanOptions()
        scanOption.setOrientationLocked(false)
        scanLauncher.launch(scanOption)
    }


}