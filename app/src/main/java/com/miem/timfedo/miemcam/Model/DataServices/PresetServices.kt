package com.miem.timfedo.miemcam.Model.DataServices

import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.json.JSON
import org.json.JSONObject

class PresetServices(private var session: Session) {

    fun setPreset(preset: Preset, completion: () -> Unit) {
        val request = BasicPostRequest(
            Session.basicAdress + "/Cam_set_preset", session.token,
            JSON.stringify(Preset.serializer(), preset), {
                completion()
            }, {})
        request.start()
    }

    fun execPreset(id: Int, velocity: ArrayList<Float>, completion: () -> Unit) {
        val json = JSONObject()
        json.put("preset_token", id)
        json.put("velocity", velocity)
        val body = json.toString()
        val request = BasicPostRequest(
            Session.basicAdress + "/Cam_goto_preset", session.token, body, {
                completion()
            }, {})
        request.start()
    }
}