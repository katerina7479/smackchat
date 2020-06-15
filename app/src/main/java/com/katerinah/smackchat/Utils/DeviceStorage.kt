package com.katerinah.smackchat.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class DeviceStorage (context: Context) {
    val TAG = "Smack ${javaClass.simpleName}"
    private val FileName = "prefs"
    private val storage: SharedPreferences = context.getSharedPreferences(FileName, 0)

    // Keys
    private val IS_LOGGED_IN = "isLoggedIn"
    private val USER_EMAIL =  "userEmail"
    private val AUTH_TOKEN = "authToken"

    var isLoggedIn: Boolean
        get() {
            return storage.getBoolean(IS_LOGGED_IN, false)
        }
        set(value) {

            storage.edit().putBoolean(IS_LOGGED_IN, value).apply()
        }

    var authToken: String
        get() {
            return storage.getString(AUTH_TOKEN, "").toString()
        }
        set(value) {
            storage.edit().putString(AUTH_TOKEN, value).apply()
        }

    var userEmail: String
        get() {
            val user_email = storage.getString(USER_EMAIL, "").toString()
            Log.d(TAG, "Get userEmail $user_email")
            return user_email
        }
        set(value) {
            Log.d(TAG, "Set userEmail $value")
            storage.edit().putString(USER_EMAIL, value).apply()
        }
}
