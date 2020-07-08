package com.deepak.nasa.utils.common

import android.os.Handler
import com.google.android.material.snackbar.Snackbar


fun showSnackBar(text : String, snackbar: Snackbar) {
    if (snackbar.isShownOrQueued) snackbar.dismiss()
    snackbar.setText(text)
    Handler().postDelayed({ snackbar.show() }, 500) //delay snackBar
}

fun hideSnackBar(snackbar: Snackbar) {
    if (snackbar.isShownOrQueued) snackbar.dismiss()
}