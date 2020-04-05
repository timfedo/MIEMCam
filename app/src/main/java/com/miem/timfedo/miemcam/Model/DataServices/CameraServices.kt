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
import org.json.JSONObject

class CameraServices(private val client: OkHttpClient, private val session: Session) {

    fun discovery(handler: (ArrayList<Camera>) -> Unit) {
        val request = BasicGetRequest(client, Session.basicAdress + "/return_cams", session.token, { response ->
            Log.e("res", response)
            handler(CamerasList.parseJson(response))
        }, { Toaster.shared.showToast("Не удвлось получить список камер") })
        request.start()
    }

    fun choseCam(uid: String, completion: (String) -> Unit, hideLoading: () -> Unit) {
        val json = JSONObject()
        json.put("uid", uid)
        val request = BasicPostRequestWithResult(client, true,Session.basicAdress + "/chose_cam", session.token, json.toString(), { result ->
            completion(result)
            hideLoading()
        }, {
            Toaster.shared.showToast("Не удалось выбрать камеру. Возможно она занята кем-то другим")
            hideLoading()
        })
        request.start()
    }

    fun releaseCamera() {
        val request = BasicPostRequest(client,Session.basicAdress + "/release_cam", session.token, null, { }, { })
        request.start()
    }
}