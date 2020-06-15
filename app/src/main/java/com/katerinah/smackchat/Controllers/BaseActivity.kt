package com.katerinah.smackchat.Controllers

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    val TAG = "Smack ${javaClass.simpleName}"
    private var spinner: ProgressBar? = null

    fun setProgressWidget(component: ProgressBar){
        spinner = component
    }

    open fun errorToast(message: String) {
        Log.e(TAG, message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    open fun enableSpinner(enable: Boolean) {
        Log.d(TAG, "Enable spinner $enable")
        if (enable) {
            spinner?.visibility = View.VISIBLE
        } else {
            spinner?.visibility = View.INVISIBLE
        }
        toggleButtons(enable)
    }

    open fun toggleButtons(enable: Boolean) {}
}
