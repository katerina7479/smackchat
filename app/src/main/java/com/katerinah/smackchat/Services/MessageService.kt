package com.katerinah.smackchat.Services

import android.util.Log
import com.android.volley.Response
import com.katerinah.smackchat.Models.Channel
import com.katerinah.smackchat.Models.Message
import com.katerinah.smackchat.Utils.*
import org.json.JSONException

object MessageService {

    val TAG = "Smack ${javaClass.simpleName}"
    val channels = mutableListOf<Channel>()
    val messages = mutableListOf<Message>()

    fun logout() {
        Log.d(TAG, "Clearing channels")
        channels.clear()
        messages.clear()
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
                    complete(false)
                }
            }, Response.ErrorListener {
                Log.d(TAG, "Could not retrieve channels: $it")
                complete(false)
            }
        )
        VolleyService.addToRequestQueue(request)
    }

    fun getMessages(channel: Channel, complete: (Boolean) -> Unit) {
        Log.d(TAG, "Getting messages for channel ${channel.name}")
        messages.clear()
        val request  = VolleyService.getAuthJsonArrayRequest(
            "${URL_GET_MESSAGES}${channel.id}",
            Response.Listener {response ->
                try {
                    for (x in 0 until response.length()) {
                        val obj = response.getJSONObject(x)
                        messages.add(Message(
                            obj.getString("messageBody"),
                            obj.getString("userName"),
                            obj.getString("channelId"),
                            obj.getString("userAvatar"),
                            obj.getString("userAvatarColor"),
                            obj.getString("_id"),
                            obj.getString("timeStamp")
                        ))
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.e(TAG, "Could not retrieve messages: $e")
                    complete(false)
                }
            }, Response.ErrorListener {
                Log.d(TAG, "Could not retrieve messages: $it")
                complete(false)
            }
        )
        VolleyService.addToRequestQueue(request)
    }
}
