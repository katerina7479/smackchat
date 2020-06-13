package com.katerinah.smackchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun onGenerateAvatarColor(view: View){
        val random  = Random()
        val colorNum = random.nextInt(2)
        val avatarNum = random.nextInt(28)

        userAvatar = if (colorNum == 0) {
            "light$avatarNum"
        } else {
            "dark$avatarNum"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        signupAvatarPreview.setImageResource(resourceId)
    }

    fun onAvatarClicked(view: View){

    }

    fun onCreateUserClicked(view: View){

    }
}