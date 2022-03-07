package com.bink.localhero.screens.scanBarcode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bink.localhero.R
import com.bink.localhero.utils.LocalStoreUtils
import com.bink.localhero.utils.LocalStoreUtils.KEY_TOKEN
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ScanBarcodeFragment : Fragment() {
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

            LocalStoreUtils.setAppSharedPref(KEY_TOKEN,token)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_barcode, container, false)
    }

    override fun onResume() {
        super.onResume()
        launchScanner()
    }

    private fun launchScanner(){
        val scanOption = ScanOptions()
        scanOption.setOrientationLocked(false)
        scanLauncher.launch(scanOption)
    }


}