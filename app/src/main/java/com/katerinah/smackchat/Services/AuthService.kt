package com.katerinah.smackchat.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request.Method.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.katerinah.smackchat.Utils.*
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest

object AuthService {

    val TAG = "Smack ${javaClass.simpleName}"

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun logout() {
        isLoggedIn = false
        userEmail = ""
        authToken = ""
    }

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(
            POST,
            URL_ACCOUNT_REGISTER,
            Response.Listener { complete(true) },
            Response.ErrorListener {response ->
                if (response.message!!.contains("is already registered")) {
                    Log.d(TAG, "User already registered")
                    complete(true)
                } else {
                    Log.e(TAG, "Could not register user: $response")
                    complete(false)
                }
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray? {
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
                    userEmail = it.getString("user")
                    isLoggedIn = true
                    complete(true)
                } catch (e: JSONException){
                    Log.e(TAG, "Could not login user: ${e.localizedMessage}")
                    complete(false)
                }
            },
            Response.ErrorListener {
                Log.e(TAG, "Could not login user: $it")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, userName: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        val requestBody = JSONObject()
        requestBody.put("email", userEmail)
        requestBody.put("name", userName)
        requestBody.put("avatarName", avatarName)
        requestBody.put("avatarColor", avatarColor)

        val request = object : JsonObjectRequest(
            URL_ADD_USER,
            requestBody,
            Response.Listener {response ->
                Log.d(TAG, "Create User Response $response")
                try {
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.id = response.getString("_id")
                    UserDataService.avatarName = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    complete(true)
                } catch (e: JSONException) {
                    Log.d(TAG, "Exception $e")
                    complete(false)
                }
            },
            Response.ErrorListener {
                Log.d(TAG, "Could not create user: $it")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(request)
    }

    fun getUserByEmail(context: Context, complete: (Boolean) -> Unit){
        val request = object: JsonObjectRequest (
            "${URL_GET_USER_BY_EMAIL}${userEmail}",
            null,
            Response.Listener { response ->
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.id = response.getString("_id")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                complete(true)
            },
            Response.ErrorListener {
                Log.e(TAG, "Could not retrieve user: $it")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
