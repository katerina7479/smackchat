package com.katerinah.smackchat.Controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import com.katerinah.smackchat.Utils.BROADCAST_USER_DATA_CHANGED
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "SmackLoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(AuthService.isLoggedIn) {
            finish()
        }
    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginLoginButton.isEnabled = !enable
        loginSignupButton.isEnabled = !enable
    }

    fun errorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun loginLoginButtonClicked(view: View){
        Log.d(TAG, "Login Button clicked")
        enableSpinner(true)
        hideKeyboard()
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
                AuthService.getUserByEmail(this, getUserCallback)
            } else errorToast("Unable to log in")
        }

        AuthService.loginUser(this, email, password, loginUserCallback)
    }

    fun loginSignupButtonClicked(view: View){
        Log.d(TAG, "Signup Button clicked")
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
        finish()
    }
}
