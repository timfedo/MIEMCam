package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Model.Toaster
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONArray
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

class ActionServices(private val client: OkHttpClient, private val session: Session) {

    fun moveCam(dir: ArrayList<Float>, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val json = JSONObject()
        json.put("ptz", JSONArray(dir))
        val request = BasicPostRequest(client, Session.basicAdress + "/continuous_move", session.token, json.toString(), {
                completion()
                Log.e("move", "move")
            }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }

    fun stop(completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val request = BasicGetRequest(client, Session.basicAdress + "/stop", session.token, {
            completion()
            Log.e("stop", "stop")
        }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }


    fun setSetting(type: SettingType, value: Float, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val typeS = when (type) {
            SettingType.B -> "brightness"
            SettingType.C -> "contrast"
            SettingType.S -> "color_saturation"
        }

        val request = BasicPostRequest(client, Session.basicAdress + "/set_" + typeS, session.token,
            JSON.stringify(FloatSerializer, value), {
                completion()
            }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }

    fun setFocusContinious(value: Float, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val json = JSONObject()
        json.put("m_foc", value)
        val request = BasicPostRequest(client, Session.basicAdress + "/move_focus_continuous", session.token, json.toString(), {
                completion()
            }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }

    fun stopFocus(completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val request = BasicGetRequest(client, Session.basicAdress + "/stop_focus", session.token, {
            completion()
            Log.e("stop", "stop")
        }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }

    fun setFocus(position: Float, speed: Float, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val json = JSONObject()
        json.put("position", position)
        json.put("speed", speed)
        val body = json.toString()
        val request = BasicPostRequest(client, Session.basicAdress + "move_focus_absolute", session.token, body, {
                completion()
            }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }

    fun setFocusMode(mode: FocusMode, completion: () -> Unit) {
        if (session.pickedCamera.isEmpty()) { return }
        val modeS = when (mode) {
            FocusMode.AUTO -> "\"AUTO\""
            FocusMode.MANUAL -> "\"MANUAL\""
        }
        Log.e("q", modeS)
        val request = BasicPostRequest(client, Session.basicAdress + "/set_focus_mode", session.token, modeS, {
            completion()
        }, { Toaster.shared.showToast("Не удвлось выполнить команду") })
        request.start()
    }
}