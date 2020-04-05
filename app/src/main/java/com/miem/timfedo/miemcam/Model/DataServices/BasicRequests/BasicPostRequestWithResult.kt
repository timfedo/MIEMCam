package com.miem.timfedo.miemcam.Model.DataServices.BasicRequests

import android.util.Log
import com.miem.timfedo.miemcam.Model.Authorizer
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class BasicPostRequestWithResult(private val client: OkHttpClient,
                                 private val showAuth: Boolean,
                                 private val url: String,
                                 private val header: String,
                                 private val body: String?,
                                 private val completion: (String) -> Unit,
                                 private val errorHandler: () -> Unit) {

    fun start() {
        val request = Request.Builder()
            .addHeader("key", header)
            .url(url)
        if (body != null) {
            val body = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            request.post(body)
        }
        client.newCall(request.build()).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body ?: return
                Log.e("q", response.message)
                when (response.code) {
                    in 200..299 ->
                        completion(responseBody.string())
                    401 ->
                        if (showAuth) {
                            unauthorized()
                        }
                    else -> {
                        Log.e("post", response.code.toString())
                        errorHandler()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("post", e.toString())
                errorHandler()
            }
        })
    }

    private fun unauthorized() {
        Authorizer.shared.showAuth()
    }
}
