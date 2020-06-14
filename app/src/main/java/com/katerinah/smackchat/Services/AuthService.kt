package com.katerinah.smackchat.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.katerinah.smackchat.Utils.URL_ACCOUNT_REGISTER
import org.json.JSONObject

object AuthService {

    const val TAG = "API ERROR"

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val url = URL_ACCOUNT_REGISTER
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { complete(true) },
            Response.ErrorListener {
                Log.d(TAG, "Could not register user: $it")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
}
