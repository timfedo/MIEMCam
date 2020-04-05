package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequestWithResult
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Model.Toaster
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject

class AuthServices(private val client: OkHttpClient) {

    fun login(email: String, password: String, completion: (String) -> Unit) {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        val request = BasicPostRequestWithResult(client, false,Session.basicAdress + "/login", "", json.toString(), { result ->
            var value = ""
            try {
                value = JSONObject(result).getString("token")
            } catch (e: JSONException) {}
            completion(value)
        }, { Toaster.shared.showToast("Неподходящие логин и пароль") })
        request.start()
    }
}