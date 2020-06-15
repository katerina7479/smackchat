package com.katerinah.smackchat.Models

import android.graphics.Color

class Message (val message: String, val userName: String, val channelId: String,
               val userAvatar: String, val userAvatarColor: String, val id: String,
               val timeStamp: String) {
    fun getAvatarColor(): Int {
        val color = userAvatarColor.trim('[', ']')
            .split(", ")
            .map {x ->
                (x.toDouble() * 255).toInt()
            }
        return Color.rgb(color[0], color[1], color[2])
    }
}
