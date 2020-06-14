package com.katerinah.smackchat.Services

import android.content.Context
import android.util.Log
import android.view.Choreographer
import com.android.volley.Request.Method.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.katerinah.smackchat.Utils.URL_ACCOUNT_LOGIN
import com.katerinah.smackchat.Utils.URL_ACCOUNT_REGISTER
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest

object AuthService {

    const val TAG = "API ERROR"

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""


    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val url = URL_ACCOUNT_REGISTER
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(
            POST,
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

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val requestBody = JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)

        val loginRequest = object : JsonObjectRequest(
            URL_ACCOUNT_LOGIN,
            requestBody,
            Response.Listener {
                Log.d(TAG, "Login Response $it")
                try {
                    authToken = it.getString("token")
                    userEmail = it.getString("email")
                    isLoggedIn = true
                    complete(true)
                } catch (e: JSONException){
                    Log.d(TAG, "Could not login user: ${e.localizedMessage}")
                    complete(false)
                }
            },
            Response.ErrorListener {
                Log.d(TAG, "Could not login user: $it")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }
}
