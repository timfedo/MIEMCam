package com.miem.timfedo.miemcam.Model.DataServices.BasicRequests

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class BasicGetRequest(private val url: String,
                      private val header: String,
                      private val completion: (String) -> Unit,
                      private val errorHandler: () -> Unit) {

    private val client = OkHttpClient()

    fun start() {
        val request = Request.Builder()
            .addHeader("header", header)
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body ?: return
                when (response.code) {
                    in 200..299 ->
                        completion(responseBody.string())
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
