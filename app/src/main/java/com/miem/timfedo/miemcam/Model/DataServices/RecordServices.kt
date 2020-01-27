package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.SoundSource
import kotlinx.serialization.json.JSON
import okhttp3.OkHttpClient
import org.json.JSONObject

class RecordServices(private val client: OkHttpClient, private val session: Session) {

    fun startRecord(id: String, completion: () -> Unit) {
        val request = BasicPostRequest(client, Session.basicAdressNvr + "/start-record/$id", session.token, null, {
            completion()
        }, {})
        request.start()
    }

    fun stopRecord(id: String, completion: () -> Unit) {
        val request = BasicPostRequest(client, Session.basicAdressNvr + "/stop-record/$id", session.token, null, {
            completion()
        }, {})
        request.start()
    }

    fun setSoundSource(id: String, sound: SoundSource, completion: () -> Unit) {
        val soundType = when (sound) {
            SoundSource.CODER -> "enc"
            SoundSource.CAMERA -> "cam"
        }
        val json = JSONObject()
        json.put("id", id)
        json.put("sound", soundType)
        val body = json.toString()
        Log.e("q", body)
        val request = BasicPostRequest(client, Session.basicAdressNvr + "/sound-change", session.token, body, {
            completion()
        }, {})
        request.start()
    }
}