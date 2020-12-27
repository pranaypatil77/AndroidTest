package com.example.android.androidtest.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


// snackbar
fun showSnackBar(message: String?, activity: Activity?) {
    if (null != activity && null != message) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        ).show()
    }
}

fun Context.toast(message : String)=
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
