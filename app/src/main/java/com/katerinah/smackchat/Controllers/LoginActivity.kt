package com.katerinah.smackchat.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "SmackLoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view: View){
        Log.d(TAG, "Login Button clicked")
        val email = LoginEmailText.text.toString()
        val password = LoginPasswordText.text.toString()
        AuthService.loginUser(this, email, password) {complete ->
            if(complete) {
                Log.d(TAG, "User logged In")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Unable to log in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loginSignupButtonClicked(view: View){
        Log.d(TAG, "Signup Button clicked")
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }
}