package com.katerinah.smackchat.Controllers

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
        val username = signupUserName.text.toString()
        val email = signupEmail.text.toString()
        val password = signupPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Enter a user and password", Toast.LENGTH_SHORT).show()
            return
        }

        AuthService.registerUser(this, email, password) {complete ->
            if (complete){
                Log.d(TAG,"User registered")
                AuthService.loginUser(this, email, password) {complete ->
                    if(complete) {
                        Log.d(TAG, "User logged In")
                        AuthService.createUser(this, username, userAvatar, avatarColor) {complete ->
                            if (complete) {
                                Log.d(TAG, "User created")
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Unable to create user", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Unable to log in", Toast.LENGTH_SHORT).show()
                    }
                }
            } else (
                Toast.makeText(this, "Unable to register user", Toast.LENGTH_SHORT).show()
            )
        }
    }
}
