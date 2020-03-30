package com.miem.timfedo.miemcam.Model.DataServices

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicGetRequest
import com.miem.timfedo.miemcam.Model.DataServices.BasicRequests.BasicPostRequest
import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import com.miem.timfedo.miemcam.Model.Session
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.jsonArray
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject

class RecordServices(private val client: OkHttpClient, private val session: Session) {

    fun getRooms(completion: (ArrayList<String>) -> Unit) {
        val request = BasicGetRequest(client, Session.basicAdressNvr + "/rooms", session.token, { result ->
            val resultArray = arrayListOf<String>()
            val jsonArray = JSONArray(result)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                resultArray.add(item.getString("name"))
            }
            completion(resultArray)
        }, {})
        request.start()
    }

    fun requestRecord(room: String, email: String, name: String, date: String, start: String, stop: String, completion: () -> Unit) {
        val json = JSONObject()
        json.put("start_time", start)
        json.put("end_time", stop)
        json.put("date", date)
        json.put("event_name", name)
        json.put("user_email", email)
        val body = json.toString()
        val request = BasicPostRequest(client, Session.basicAdressNvr + "/montage-event/$room", session.token, body, completion, {})
        request.start()
    }
}