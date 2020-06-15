package com.katerinah.smackchat.Controllers

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import com.katerinah.smackchat.Utils.*
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : BaseActivity() {

    private var userAvatar = "profileDefault"
    private var avatarColor = "[0.5, 0.5, 0.5, 1]"
    private val random  = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        setProgressWidget(createSpinner)
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

    override fun toggleButtons(enable: Boolean) {
        createUserButton.isEnabled = !enable
        avatarColorButton.isEnabled = !enable
        signupAvatarPreview.isEnabled = !enable
    }

    fun onCreateUserClicked(view: View){
        Log.d(TAG, "User Create")
        enableSpinner(true)
        val username = signupUserName.text.toString()
        val email = signupEmail.text.toString()
        val password = signupPassword.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
            errorToast("Please enter all information")
            return
        }

        val createUserCallback = {complete: Boolean ->
            if (complete) {
                Log.d(TAG, "User created")
                val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGED)
                LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChanged)
                enableSpinner(false)
                finish()
            } else errorToast("Unable to create user")
        }

        val loginUserCallback = {complete: Boolean ->
            if (complete) {
                Log.d(TAG, "User logged In")
                AuthService.createUser(username, userAvatar, avatarColor, createUserCallback)
            } else errorToast("Unable to login")
        }

        val registerUserCallback = {complete: Boolean ->
            if (complete) {
                Log.d(TAG,"User registered")
                AuthService.loginUser(email, password, loginUserCallback)
            } else errorToast("Unable to register user")
        }

        AuthService.registerUser(email, password, registerUserCallback)
    }
}
