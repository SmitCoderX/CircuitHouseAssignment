package com.smitcoderx.circuithouseassignment.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CircuitHouseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}