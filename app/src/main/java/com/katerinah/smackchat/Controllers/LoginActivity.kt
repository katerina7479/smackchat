package com.katerinah.smackchat.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.katerinah.smackchat.R

class LoginActivity : AppCompatActivity() {
    private val TAG = "SmackLoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view: View){
        Log.d(TAG, "Login Button clicked")
    }

    fun loginSignupButtonClicked(view: View){
        Log.d(TAG, "Signup Button clicked")
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }
}