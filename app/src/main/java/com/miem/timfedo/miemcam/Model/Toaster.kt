package com.miem.timfedo.miemcam.Model

import android.app.Activity
import android.widget.Toast

class Toaster {

    companion object {
        val shared = Toaster()
    }

    var activity: Activity? = null

    fun showToast(text: String) {
        activity?.runOnUiThread {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }
}