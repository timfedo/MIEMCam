package com.miem.timfedo.miemcam.Model.DataServices.BasicRequests

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class BasicGetRequest(private val client: OkHttpClient,
                      private val url: String,
                      private val header: String,
                      private val completion: (String) -> Unit,
                      private val errorHandler: () -> Unit) {

    fun start() {
        val request = Request.Builder()
            .addHeader("key", header)
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.e("q", response.message)
                val responseBody = response.body ?: return
                when (response.code) {
                    in 200..299 ->
                        completion(responseBody.string())
                    401 ->
                        unauthorized()
                    else -> {
                        Log.e("post", response.code.toString())
                        errorHandler()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("get", "fail")
                errorHandler()
            }
        })
    }

    private fun unauthorized() {
        //TODO: in next version with authorization
    }
}
