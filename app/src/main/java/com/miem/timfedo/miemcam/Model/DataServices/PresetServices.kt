package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Model.Toaster
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONObject

class PresetServices(private val client: OkHttpClient, private val session: Session) {

    fun setPreset(preset: Preset, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val request = BasicPostRequest(
            client, Session.basicAdress + "/Cam_set_preset", session.token,
            JSON.stringify(Preset.serializer(), preset), {
                completion()
            }, { Toaster.shared.showToast("Не удвлось сохранить пресет") })
        request.start()
    }

    fun setPreset(id: Int, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val json = JSONObject()
        json.put("s_pres", id)
        val request = BasicPostRequest(
            client, Session.basicAdress + "/set_preset", session.token, json.toString(), {
                completion()
            }, { Toaster.shared.showToast("Не удвлось сохранить пресет") })
        request.start()
    }

    fun execPreset(id: Int, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val json = JSONObject()
        json.put("go_pres", id)
        val body = json.toString()
        val request = BasicPostRequest(
            client, Session.basicAdress + "/goto_preset", session.token, body, {
                completion()
            }, { Toaster.shared.showToast("Не удвлось выполнить пресет") })
        request.start()
    }
}