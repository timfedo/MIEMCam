package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequestWithResult
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONObject

class AuthServices(private val client: OkHttpClient) {

    fun login(email: String, password: String, completion: (String) -> Unit) {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        val request = BasicPostRequestWithResult(client, Session.basicAdressNvr + "/login", "", json.toString(), { result ->
            completion(JSONObject(result).getString("token"))
        }, {})
        request.start()
    }
}