package com.katerinah.smackchat.Controllers


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import com.katerinah.smackchat.Utils.BROADCAST_USER_DATA_CHANGED
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(App.deviceStorage.isLoggedIn) {
            finish()
        }
        setProgressWidget(loginSpinner)
    }

    override fun toggleButtons(enable: Boolean) {
        loginLoginButton.isEnabled = !enable
        loginSignupButton.isEnabled = !enable
    }

    fun loginLoginButtonClicked(view: View){
        Log.d(TAG, "Login Button clicked")
        enableSpinner(true)
        val email = LoginEmailText.text.toString()
        val password = LoginPasswordText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            errorToast("Please enter all information")
            return
        }

        val getUserCallback = {complete: Boolean ->
            if (complete) {
                enableSpinner(false)
                val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGED)
                LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChanged)
                finish()
            } else errorToast("Unable to retrieve profile")
        }

        val loginUserCallback = {complete: Boolean ->
            if (complete) {
                Log.d(TAG, "User logged In")
                AuthService.getUserByEmail(getUserCallback)
            } else errorToast("Unable to log in")
        }

        AuthService.loginUser(email, password, loginUserCallback)
    }

    fun loginSignupButtonClicked(view: View){
        Log.d(TAG, "Signup Button clicked")
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
        finish()
    }
}
