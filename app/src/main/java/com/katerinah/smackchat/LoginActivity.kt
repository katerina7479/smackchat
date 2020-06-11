package com.katerinah.smackchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view: View){

    }

    fun loginSignupButtonClicked(view: View){
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }
}