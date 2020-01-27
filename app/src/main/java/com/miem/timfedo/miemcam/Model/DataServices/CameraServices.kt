package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONObject

class CameraServices(private val client: OkHttpClient, private val session: Session) {

    fun discovery(handler: (ArrayList<Camera>) -> Unit) {
        val request = BasicGetRequest(client, Session.basicAdress + "/return_cams", session.token, { response ->
            Log.e("res", response)
            handler(CamerasList.parseJson(response))
        }, {})
        request.start()
    }

    fun choseCam(uid: String, completion: () -> Unit) {
        val json = JSONObject()
        json.put("uid", uid)
        val request = BasicPostRequest(client, Session.basicAdress + "/chose_cam", session.token, json.toString(), {
            completion()
        }, {})
        request.start()
    }
}