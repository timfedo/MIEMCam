package com.miem.timfedo.miemcam.Model

class Authorizer {

    companion object {
        val shared = Authorizer()
    }

    var showAuth: () -> Unit = { }
}