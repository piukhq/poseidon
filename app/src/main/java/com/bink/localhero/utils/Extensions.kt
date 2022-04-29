package com.bink.localhero.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showDialog(
    title: String? = null,
    message: String? = null,
    positiveBtn: String? = null,
    negativeBtn: String?= null,
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