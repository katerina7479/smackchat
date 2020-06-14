package com.katerinah.smackchat.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.katerinah.smackchat.Models.Channel
import com.katerinah.smackchat.Utils.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {

    val TAG = "Smack ${javaClass.simpleName}"
    val channels = mutableListOf<Channel>()

    fun logout() {
        Log.d(TAG, "Clearing channels")
        channels.clear()
    }

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {
        Log.d(TAG, "Getting channels")
        val request = object: JsonArrayRequest(
            URL_GET_CHANNELS,
            Response.Listener {response ->
                try {
                    for (x in 0 until response.length()) {
                        val obj = response.getJSONObject(x)
                        channels.add(Channel(
                            obj.getString("name"),
                            obj.getString("description"),
                            obj.getString("_id")))
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.e(TAG, "Could not retrieve channels: $e")
                }
            }, Response.ErrorListener {
                Log.d(TAG, "Could not retrieve channels: $it")
                complete(false)
            }
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

        Volley.newRequestQueue(context).add(request)
    }
}
