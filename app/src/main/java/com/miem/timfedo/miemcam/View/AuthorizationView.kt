package com.miem.timfedo.miemcam.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miem.timfedo.miemcam.Model.DataServices.AuthServices
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.activity_authorization_view.*
import okhttp3.OkHttpClient
import android.app.Activity
import android.content.Intent


class AuthorizationView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_view)
        loginBtn.setOnClickListener {
            loginBtnPressed(emailField.text.toString(), passwordField.text.toString())
        }
    }

    override fun onBackPressed() {

    }

    private fun loginBtnPressed(email: String, password: String) {
        val client = OkHttpClient()
        val authServices = AuthServices(client)
        authServices.login(email, password) { result ->
            runOnUiThread {
                val resultIntent = Intent()
                resultIntent.putExtra("token", result)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
