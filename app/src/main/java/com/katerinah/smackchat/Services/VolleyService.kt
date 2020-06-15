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
import com.katerinah.smackchat.Controllers.App
import org.json.JSONArray
import org.json.JSONObject


object VolleyService {
    private var context: Context?  = null

    private val queue: RequestQueue by lazy {
        if (context == null) {
            throw NullPointerException(" Initialize VolleyService in application class")
        } else {
            Volley.newRequestQueue(context)
        }
    }

    fun init(context: Context) {
        this.context = context
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
                headers.put("Authorization", "Bearer ${App.deviceStorage.authToken}")
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
                headers.put("Authorization", "Bearer ${App.deviceStorage.authToken}")
                return headers
            }
        }
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        queue.add(req)
    }
}
