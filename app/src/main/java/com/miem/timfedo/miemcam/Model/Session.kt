package com.miem.timfedo.miemcam.Model

class Session(var token: String) {

    companion object {
        val basicAdressNvr = "https://nvr.miem.hse.ru/api"
        var basicAdress = "http://92.53.78.98:80"
    }

    var pickedRoom = ""
}