package com.miem.timfedo.miemcam.Model

import android.app.Activity
import android.content.Context

class Session(val context: Context) {

    var key = "9445980805164d5aa0efc6c91500d298"
    var token: String = ""
    set(newToken) {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("token_key", newToken)
            apply()
        }
        field = newToken
    }

    var email: String = ""
        set(newEmail) {
            val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("email_key", newEmail)
                apply()
            }
            field = newEmail
        }

    init {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        token = sharedPref.getString("token_key", "") ?: ""
        email = sharedPref.getString("email_key", "") ?: ""
    }

    companion object {
        val basicAdressNvr = "https://nvr.miem.hse.ru/api"
        var basicAdress = "http://92.53.78.98:80"
    }

    fun clean() {
        token = ""
        email = ""
        pickedRoom = ""
        pickedCamera = ""
        port = ""
    }

    var pickedRoom = ""
    var pickedCamera = ""
    var port = ""
}