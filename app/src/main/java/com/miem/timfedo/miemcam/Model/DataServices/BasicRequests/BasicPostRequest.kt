package com.miem.timfedo.miemcam.Model.DataServices.BasicRequests

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class BasicPostRequest(private val url: String,
                       private val header: String,
                       private val body: String,
                       private val completion: () -> Unit,
                       private val errorHandler: () -> Unit) {

    private val client = OkHttpClient()

    fun start() {
        val body = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .addHeader("header", header)
            .url(url)
            .post(body)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    in 200..299 ->
                        completion()
                    401 ->
                        unauthorized()
                    in 500..599 ->
                        errorHandler()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                errorHandler()
            }
        })
    }

    private fun unauthorized() {
        //TODO: in next version with authorization
    }
}
