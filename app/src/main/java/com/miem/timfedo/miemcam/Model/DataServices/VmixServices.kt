package com.miem.timfedo.miemcam.Model.DataServices

import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Session
import okhttp3.OkHttpClient
import org.json.JSONObject

class VmixServices(private val client: OkHttpClient, private val session: Session) {

    fun executeFunction(function: String, input: String?, completion: () -> Unit) {
        val json = JSONObject()
        json.put("Function", function)
        if (input != null) {
            json.put("Input", input)
        }
        val request = BasicPostRequest(client, Session.basicAdress + "/vmix", session.token, json.toString(), {
            completion()
        }, {})
        request.start()
    }
}