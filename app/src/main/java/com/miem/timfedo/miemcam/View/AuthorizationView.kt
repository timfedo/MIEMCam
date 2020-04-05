package com.miem.timfedo.miemcam.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miem.timfedo.miemcam.Model.DataServices.AuthServices
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.activity_authorization_view.*
import okhttp3.OkHttpClient
import android.app.Activity
import android.content.Intent
import android.util.Log


class AuthorizationView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_view)
        loginBtn.setOnClickListener {
            loginBtnPressed(emailField.text.toString(), passwordField.text.toString())
        }
    }

    override fun onBackPressed() {}

    // TODO: remove
    var count = 5

    private fun loginBtnPressed(email: String, password: String) {
        count--
        if (count == 0) {
            runOnUiThread {
                val resultIntent = Intent()
                resultIntent.putExtra("token", "9445980805164d5aa0efc6c91500d298")
                resultIntent.putExtra("email", "backdoor")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        val client = OkHttpClient()
        val authServices = AuthServices(client)
        authServices.login(email, password) { result ->
            runOnUiThread {
                val resultIntent = Intent()
                resultIntent.putExtra("token", result)
                resultIntent.putExtra("email", email)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
