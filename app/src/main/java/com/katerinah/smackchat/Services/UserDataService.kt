package com.katerinah.smackchat.Services

import android.graphics.Color
import com.katerinah.smackchat.Utils.*

object UserDataService {

    val defaultAvatar = DEFAULT_AVATAR
    val defaultColor = DEFAULT_COLOR

    var id = ""
    var avatarName = defaultAvatar
    var avatarColor = defaultColor
    var email = ""
    var name = ""

    fun getAvatarColor(): Int {
        val color = avatarColor.trim('[', ']')
            .split(", ")
            .map {x ->
                (x.toDouble() * 255).toInt()
            }
        return Color.rgb(color[0], color[1], color[2])
    }

    fun logout() {
        id = ""
        avatarName = defaultAvatar
        avatarColor = defaultColor
        email = ""
        name = ""
    }
}
