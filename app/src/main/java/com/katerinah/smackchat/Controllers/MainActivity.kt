package com.katerinah.smackchat.Controllers

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import com.katerinah.smackchat.Services.UserDataService
import com.katerinah.smackchat.Utils.BROADCAST_USER_DATA_CHANGED
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "SmackMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        updateHeader()
        updateMain()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            UserDataChangedReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGED))
    }

    private val UserDataChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateHeader()
            updateMain()
        }
    }

    fun updateHeader(){
        if (AuthService.isLoggedIn) {
            loginButtonNavHeader.text = "Logout"
        } else {
            loginButtonNavHeader.text = "Login"
        }
        profileNameNavHeader.text = UserDataService.name
        profileEmailNavHeader.text = UserDataService.email
        val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
        profileImageNavHeader.setImageResource(resourceId)
        profileImageNavHeader.setBackgroundColor(UserDataService.getAvatarColor())
    }

    fun updateMain(){
        if (AuthService.isLoggedIn) {
            mainChannelName.text = ""
        } else {
            mainChannelName.text = getString(R.string.please_log_in)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginButtonNavClicked(view: View) {
        if (AuthService.isLoggedIn) {
            Log.d(TAG, "Logout Button clicked")
            UserDataService.logout()
            AuthService.logout()
            updateHeader()
            updateMain()
        } else {
            Log.d(TAG, "Login Button clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun addChannelClicked(view: View){
        if (AuthService.isLoggedIn) {
            Log.d(TAG, "Add channel clicked")
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_modal, null)
            builder.setView(dialogView).setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                val nameField = dialogView.findViewById<EditText>(R.id.addChannelNameText)
                val descField = dialogView.findViewById<EditText>(R.id.addChannelDescText)
                val channelNameText = nameField.text.toString()
                val descText = descField.text.toString()
                Log.d(TAG, "Creating channel $channelNameText, $descText")
            }.setNegativeButton("Cancel") { _, _ ->
            }.show()
        }
    }

    fun sendButtonClicked(view: View){
        Log.d(TAG, "Message send button clicked")
    }
}
