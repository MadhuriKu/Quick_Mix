package com.orpat.quickmix.utility

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun showSnackbar(message: String,view : View) {
        Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_SHORT
        ).show()
    }

    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}