package com.bink.localhero.screens.settings

import android.app.AlertDialog
import android.view.LayoutInflater
import com.bink.localhero.base.BaseFragment
import com.bink.localhero.base.MainActivity
import com.bink.localhero.databinding.FragmentSettingsBinding
import com.bink.localhero.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override val viewModel: SettingsViewModel by viewModel()

    override fun setup() {

        with(binding) {

            LocalStoreUtils.getAppSharedPref(LocalStoreUtils.KEY_TOKEN)?.let { loginJWT ->
                JWTUtils.decode(loginJWT)?.let { tokenJson ->
                    tvCurrentLogin.text = JWTUtils.getEmailFromJson(tokenJson) ?: ""
                }

                ivBarcode.setImageBitmap(loginJWT.toQRCode())
            }

            liChangeEnvironment.setOnClickListener {
                displayEnvironmentPicker()
            }

            tvChangeEnvironment.text = SharedPreferenceManager.storedApiUrl

            liLogout.setOnClickListener {
                (requireActivity() as MainActivity).forceRunApp()
            }
        }

    }

    private fun displayEnvironmentPicker() {
        val adb = AlertDialog.Builder(requireContext())
        val items = arrayOf<CharSequence>(ApiEnvironment.DEV.name, ApiEnvironment.STAGING.name)

        var selection = -1
        adb.setSingleChoiceItems(items, selection) { _, index ->
            selection = index
        }

        adb.setPositiveButton(
            "Ok"
        ) { _, _ ->
            when (selection) {
                0 -> SharedPreferenceManager.storedApiUrl = ApiEnvironment.DEV.url
                1 -> SharedPreferenceManager.storedApiUrl = ApiEnvironment.STAGING.url
            }
        }
        adb.setNegativeButton("Cancel", null)
        adb.show()
    }
}