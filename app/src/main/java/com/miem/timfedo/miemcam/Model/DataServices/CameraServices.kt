package com.miem.timfedo.miemcam.Model.DataServices

import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.json.JSON
import org.json.JSONObject

class CameraServices(private var session: Session) {

    fun discovery(handler: (HashMap<String, ArrayList<Camera>>) -> Unit) {
        val request = BasicGetRequest(Session.basicAdress + "/Discovery", session.token, { response ->
            handler(CamerasList.parseJson(response))
        }, {})
        request.start()
    }

    fun addCamera(ip: String, port: String, logpass: String, camera: Camera, completion: () -> Unit) {
        val json = JSONObject()
        json.put("ip", ip)
        json.put("port", port)
        json.put("logpass", logpass)
        val body = json.toString() + JSON.stringify(Camera.serializer(), camera)
        val request = BasicPostRequest(Session.basicAdress + "/Add_Camera", session.token, body, {
            completion()
        }, {})
        request.start()
    }

    fun deleteCamera(camera: Camera, completion: () -> Unit) {
        val request = BasicPostRequest(Session.basicAdress + "/Delete_Cam", session.token,
            JSON.stringify(Camera.serializer(), camera), {
                completion()
            }, {})
    }
}