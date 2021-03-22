package com.jetwiz.akudimana.base

import android.app.Application
import com.androidnetworking.AndroidNetworking
import timber.log.Timber

class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidNetworking.initialize(getApplicationContext());
    }
}