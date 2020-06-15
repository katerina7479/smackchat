package com.katerinah.smackchat.Controllers

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.katerinah.smackchat.Models.Channel
import com.katerinah.smackchat.R
import com.katerinah.smackchat.Services.AuthService
import com.katerinah.smackchat.Services.MessageService
import com.katerinah.smackchat.Services.UserDataService
import com.katerinah.smackchat.Utils.*
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity() {
    lateinit var channelAdapter: ArrayAdapter<Channel>
    val socketClient: Socket = IO.socket(SOCKET_URL)
    var clientConnected: Boolean = false
    var receiverRegistered: Boolean = false

    private fun setupAdapters() {
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channelList.adapter = channelAdapter
    }

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

        if (App.deviceStorage.isLoggedIn) {
            restartLoggedIn(this)
        }
        setupAdapters()
        updateHeader()
        updateMain()
    }

    override fun onStart() {
        super.onStart()
        if (receiverRegistered){
            Log.d(TAG, "UnRegistering Receiver")
            LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(_userDataChangedReceiver)
            receiverRegistered = false
        }
        if (App.deviceStorage.isLoggedIn && !clientConnected) {
            Log.d(TAG, "Connecting Socket")
            socketClient.connect()
            clientConnected = true
            Log.d(TAG, "Socket Listener")
            socketClient.on(CHANNEL_CREATED, onNewChannel)
        }
    }

    override fun onStop() {
        super.onStop()
        if (clientConnected){
            Log.d(TAG, "Client disconnected")
            socketClient.disconnect()
        }
        if (!receiverRegistered) {
            Log.d(TAG, "Registering Receiver")
            LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                    _userDataChangedReceiver,
                    IntentFilter(BROADCAST_USER_DATA_CHANGED))
            receiverRegistered = true
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    val onChannelUpdate = {complete: Boolean ->
        if (complete) {
            Log.d(TAG, "Notify adapter channel updated")
            channelAdapter.notifyDataSetChanged()
        } else errorToast("Unable to update channels")
    }

    private val _userDataChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            Log.d(TAG, "Client changed, updating")
            updateHeader()
            updateMain()
            MessageService.getChannels(context, onChannelUpdate)
        }
    }

    fun restartLoggedIn(context: Context) {
        Log.d(TAG, "App restarted, re-aquiring user")

        val getUserCallback: (Boolean) -> Unit = {complete: Boolean ->
            if (complete) {
                Log.d(TAG, "Logged back in")
                updateHeader()
                updateMain()
                MessageService.getChannels(context, onChannelUpdate)
            } else {
                Log.e(TAG, "Auth has expired")
                Toast.makeText(this, "Login Expired, please login again", Toast.LENGTH_LONG).show()
                enableSpinner(false)
            }
        }
        AuthService.getUserByEmail(this, getUserCallback)
    }

    fun updateHeader(){
        if (App.deviceStorage.isLoggedIn) {
            loginButtonNavHeader.text = getString(R.string.logout)
        } else {
            loginButtonNavHeader.text = getString(R.string.login)
        }
        profileNameNavHeader.text = UserDataService.name
        profileEmailNavHeader.text = UserDataService.email
        val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
        profileImageNavHeader.setImageResource(resourceId)
        profileImageNavHeader.setBackgroundColor(UserDataService.getAvatarColor())
    }

    fun updateMain(){
        if (App.deviceStorage.isLoggedIn) {
            mainChannelName.text = ""
        } else {
            mainChannelName.text = getString(R.string.please_log_in)
        }
    }

    fun loginButtonNavClicked(view: View) {
        if (App.deviceStorage.isLoggedIn) {
            Log.d(TAG, "Logout Button clicked")
            UserDataService.logout()
            AuthService.logout()
            MessageService.logout()
            channelAdapter.notifyDataSetChanged()
            updateHeader()
            updateMain()
        } else {
            Log.d(TAG, "Login Button clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun addChannelClicked(view: View){
        if (App.deviceStorage.isLoggedIn) {
            Log.d(TAG, "Add channel clicked")
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_modal, null)
            builder.setView(dialogView).setPositiveButton("Add") { _: DialogInterface, _: Int ->
                val nameField = dialogView.findViewById<EditText>(R.id.addChannelNameText)
                val descField = dialogView.findViewById<EditText>(R.id.addChannelDescText)
                val channelNameText = nameField.text.toString()
                val descText = descField.text.toString()
                Log.d(TAG, "Emitting Event add_channel $channelNameText, $descText")
                socketClient.emit(NEW_CHANNEL, channelNameText, descText)
            }.setNegativeButton("Cancel") { _, _ ->
            }.show()
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        Log.d(TAG, "Got new channel from listener $args")
        val name: String = args[0].toString()
        val description: String = args[1] as String
        val id: String = args[2] as String

        runOnUiThread {
            MessageService.channels.add(Channel(name, description, id))
            channelAdapter.notifyDataSetChanged()
        }
    }

    fun sendButtonClicked(view: View){
        Log.d(TAG, "Message send button clicked")
    }
}
