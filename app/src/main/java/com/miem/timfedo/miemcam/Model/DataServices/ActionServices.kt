package com.miem.timfedo.miemcam.Model.DataServices

import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.json.JSON
import org.json.JSONObject

enum class SettingType {
    B,
    C,
    S
}

enum class FocusMode {
    AUTO,
    MANUAL
}

class ActionServices(private var session: Session) {

    fun moveCam(dir: ArrayList<Float>, completion: () -> Unit) {
        val request = BasicPostRequest(Session.basicAdress + "/Cam_move_continious", session.token,
            JSON.stringify(ArrayListSerializer(FloatSerializer), dir), {
                completion()
            }, {})
        request.start()
    }

    fun setSetting(type: SettingType, value: Float, completion: () -> Unit) {
        val typeS = when (type) {
            SettingType.B -> "brigthness"
            SettingType.C -> "contrast"
            SettingType.S -> "saturation"
        }

        val request = BasicPostRequest(Session.basicAdress + "/Cam_set_" + typeS, session.token,
            JSON.stringify(FloatSerializer, value), {
                completion()
            }, {})
        request.start()
    }

    fun setFocusContinious(value: Float, completion: () -> Unit) {
        val request = BasicPostRequest(Session.basicAdress + "/Cam_move_focus_continious", session.token,
            JSON.stringify(FloatSerializer, value), {
                completion()
            }, {})
        request.start()
    }

    fun setFocus(position: Float, speed: Float, completion: () -> Unit) {
        val json = JSONObject()
        json.put("position", position)
        json.put("speed", speed)
        val body = json.toString()
        val request = BasicPostRequest(Session.basicAdress + "/Cam_move_focus_absolute", session.token, body, {
                completion()
            }, {})
        request.start()
    }

    fun setFocusMode(mode: FocusMode, completion: () -> Unit) {
        val modeS = when (mode) {
            FocusMode.AUTO -> "AUTO"
            FocusMode.MANUAL -> "MANUAL"
        }
        val request = BasicPostRequest(Session.basicAdress + "/Cam_set_focus_mode", session.token, modeS, {
            completion()
        }, {})
        request.start()
    }
}