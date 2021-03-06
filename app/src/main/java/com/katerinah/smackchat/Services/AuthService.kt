package com.katerinah.smackchat.Services

import android.util.Log
import com.android.volley.Request.Method.*
import com.android.volley.Response
import com.katerinah.smackchat.Controllers.App
import com.katerinah.smackchat.Utils.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    val TAG = "Smack ${javaClass.simpleName}"

    fun logout() {
        App.deviceStorage.isLoggedIn = false
        App.deviceStorage.userEmail = ""
        App.deviceStorage.authToken = ""
    }

    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val request = VolleyService.getStringRequest(
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
            },
            requestBody
        )
        VolleyService.addToRequestQueue(request)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit){
        val requestBody = JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)

        val request = VolleyService.getJsonObjectRequest(
            URL_ACCOUNT_LOGIN,
            requestBody,
            Response.Listener {
                Log.d(TAG, "Login Response $it")
                try {
                    App.deviceStorage.authToken = it.getString("token")
                    App.deviceStorage.userEmail = it.getString("user")
                    App.deviceStorage.isLoggedIn = true
                    complete(true)
                } catch (e: JSONException){
                    Log.e(TAG, "Could not login user: ${e.localizedMessage}")
                    complete(false)
                }
            },
            Response.ErrorListener {
                Log.e(TAG, "Could not login user: $it")
                complete(false)
            }
        )
        VolleyService.addToRequestQueue(request)
    }

    fun createUser(userName: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        val requestBody = JSONObject()
        requestBody.put("email", App.deviceStorage.userEmail)
        requestBody.put("name", userName)
        requestBody.put("avatarName", avatarName)
        requestBody.put("avatarColor", avatarColor)

        val request = VolleyService.getAuthJsonObjectRequest(
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
            }
        )
        VolleyService.addToRequestQueue(request)
    }

    fun getUserByEmail(complete: (Boolean) -> Unit){

        val request = VolleyService.getAuthJsonObjectRequest(
            "${URL_GET_USER_BY_EMAIL}${App.deviceStorage.userEmail}",
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
            }
        )
        VolleyService.addToRequestQueue(request)
    }
}
