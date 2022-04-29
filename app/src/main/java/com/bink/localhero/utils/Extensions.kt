package com.bink.localhero.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

fun Context.showDialog(
    title: String? = null,
    message: String? = null,
    positiveBtn: String? = null,
    negativeBtn: String? = null,
    cancelable: Boolean = false,
    positiveCallback: () -> Unit = {},
    negativeCallback: () -> Unit = {}
) {
    val builder = AlertDialog.Builder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveBtn) { _, _ ->
            positiveCallback()
        }
        setNegativeButton(negativeBtn) { _, _ ->
            negativeCallback()
        }
        setCancelable(cancelable)
        create()
    }
    builder.show()
}

fun String.toQRCode(): Bitmap {
    return BarcodeEncoder().encodeBitmap(this, BarcodeFormat.QR_CODE, 512, 512)
}

fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}