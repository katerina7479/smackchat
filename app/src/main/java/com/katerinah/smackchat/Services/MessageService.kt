package com.katerinah.smackchat.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
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

    fun getChannels(complete: (Boolean) -> Unit) {
        Log.d(TAG, "Getting channels")
        val request = VolleyService.getAuthJsonArrayRequest(
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
        )
        VolleyService.addToRequestQueue(request)
    }
}
