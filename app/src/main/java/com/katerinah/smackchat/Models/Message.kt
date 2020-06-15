package com.katerinah.smackchat.Models

import android.graphics.Color
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Message (val message: String, val userName: String, val channelId: String,
               val userAvatar: String, val userAvatarColor: String, val id: String,
               val timeStamp: String) {
    val TAG = "Smack MessModel"

    fun getAvatarColor(): Int {
        val color = userAvatarColor.trim('[', ']')
            .split(", ")
            .map {x ->
                (x.toDouble() * 255).toInt()
            }
        return Color.rgb(color[0], color[1], color[2])
    }

    fun formattedTimestamp(): String {
        val prettyformatter = SimpleDateFormat("E, h:mm a", Locale.getDefault())
        val isoformatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoformatter.timeZone = TimeZone.getTimeZone("UTC")

        var date_object = Date()
        try {
            date_object = isoformatter.parse(timeStamp)
        } catch (e: ParseException) {
            Log.d(TAG, "Cannot parse date")
        }
        return prettyformatter.format(date_object)
    }
}
