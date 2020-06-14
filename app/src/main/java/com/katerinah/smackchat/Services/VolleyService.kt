package com.katerinah.smackchat.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class VolleyService(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleyService? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyService(context).also {
                    INSTANCE = it
                }
        }

        fun getStringRequest(method: Int, url: String, listener: Listener<String>, errorListener: Response.ErrorListener, requestBody: String): StringRequest {
            return object : StringRequest(method, url, listener, errorListener){
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray? {
                    return requestBody.toByteArray()
                }
            }
        }

        fun getJsonObjectRequest(url: String, jsonRequest: JSONObject, listener: Listener<JSONObject>, errorListener: Response.ErrorListener): JsonObjectRequest{
            return object : JsonObjectRequest(
                url,
                jsonRequest,
                listener,
                errorListener
            ) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }
        }

        fun getAuthJsonObjectRequest(url: String, jsonRequest: JSONObject?, listener: Listener<JSONObject>, errorListener: Response.ErrorListener): JsonObjectRequest{
            return object : JsonObjectRequest(
                url,
                jsonRequest,
                listener,
                errorListener
            ) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${AuthService.authToken}")
                    return headers
                }
            }
        }

        fun getAuthJsonArrayRequest(url: String, listener: Listener<JSONArray>, errorListener: Response.ErrorListener): JsonArrayRequest {
            return object: JsonArrayRequest(
                url,
                listener,
                errorListener
            ) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${AuthService.authToken}")
                    return headers
                }
            }
        }
    }

    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}
