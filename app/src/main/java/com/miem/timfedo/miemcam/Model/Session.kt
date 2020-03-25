package com.miem.timfedo.miemcam.Model

import android.app.Activity
import android.content.Context

class Session(val context: Context) {

    var token: String = "9445980805164d5aa0efc6c91500d298"
    set(newToken) {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("token_key", newToken)
            apply()
        }
        field = newToken
    }

    init {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        //token = sharedPref.getString("token_key", "") ?: ""
    }

    companion object {
        val basicAdressNvr = "https://nvr.miem.hse.ru/api"
        var basicAdress = "http://92.53.78.98:80"
    }

    var pickedRoom = ""
    var pickedCamera = ""
    var port = ""
}