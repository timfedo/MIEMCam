package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONObject

class PresetServices(private val client: OkHttpClient, private val session: Session) {

    fun setPreset(preset: Preset, completion: () -> Unit) {
        val request = BasicPostRequest(
            client, Session.basicAdress + "/Cam_set_preset", session.token,
            JSON.stringify(Preset.serializer(), preset), {
                completion()
            }, {})
        request.start()
    }

    fun execPreset(id: Int, completion: () -> Unit) {
        val json = JSONObject()
        json.put("go_pres", id)
        val body = json.toString()
        val request = BasicPostRequest(
            client, Session.basicAdress + "/goto_preset", session.token, body, {
                completion()
            }, {})
        request.start()
    }
}