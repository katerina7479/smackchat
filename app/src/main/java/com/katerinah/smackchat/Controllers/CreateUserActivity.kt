package com.katerinah.smackchat.Controllers

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    private val TAG = "SmackCreateUser"

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"
    val random  = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun onAvatarClicked(view: View){
        Log.d(TAG, "Generate Avatar Image")
        val colorNum = random.nextInt(2)
        val avatarNum = random.nextInt(28)

        userAvatar = if (colorNum == 0) {
            "light$avatarNum"
        } else {
            "dark$avatarNum"
        }
        Log.d(TAG, "User avatar $userAvatar")

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        signupAvatarPreview.setImageResource(resourceId)
    }

    fun onGenerateAvatarColor(view: View){
        Log.d(TAG, "Generate Avatar Color")
        val redNum = random.nextInt(255)
        val blueNum = random.nextInt(255)
        val greenNum = random.nextInt(255)
        signupAvatarPreview.setBackgroundColor(Color.rgb(redNum, blueNum, greenNum))

        Log.d(TAG, "Convert for db")
        val red = redNum.toDouble() / 255
        val green = blueNum.toDouble() / 255
        val blue = greenNum.toDouble() / 255
        avatarColor = "[$red, $green, $blue, 1]"
    }

    fun onCreateUserClicked(view: View){
        Log.d(TAG, "User Create")
        AuthService.registerUser(this, "k@k.com", "123456") {complete ->
            if (complete){
                Log.d(TAG,"User was created")
            }
        }
    }
}