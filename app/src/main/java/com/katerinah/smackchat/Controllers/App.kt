package com.katerinah.smackchat.Controllers

import android.app.Application
import com.katerinah.smackchat.Services.VolleyService
import com.katerinah.smackchat.Utils.DeviceStorage

class App : Application() {

    companion object {
        lateinit var deviceStorage: DeviceStorage
    }

    override fun onCreate(){
        deviceStorage = DeviceStorage(applicationContext)
        VolleyService.init(applicationContext)
        super.onCreate()
    }
}
