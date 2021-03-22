package com.jetwiz.akudimana.util

import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.configuration.PermissionConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.listener.LocationListener
import timber.log.Timber

class U_Maps(val activity: Fragment, lifecycle: Lifecycle, var mapsUtilListener: MapsUtilListener):LifecycleObserver, LocationListener {

    interface MapsUtilListener {
        fun onLocationChanged(location: Location?) {}
        fun onLocationFailed(type: Int) {}
    }

    var locationManager: LocationManager? = null
        private set

    private val locationConfiguration = LocationConfiguration.Builder()
        .askForPermission(PermissionConfiguration.Builder()
            .rationaleMessage("Gimme the permission!")
            .build())
        .useGooglePlayServices(GooglePlayServicesConfiguration.Builder()
            .build())
        .useDefaultProviders(DefaultProviderConfiguration.Builder()
            .gpsMessage("Turn on GPS?")
            .build()
        ).build()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onCreate() {
        LocationManager.enableLog(true)

        locationManager = LocationManager.Builder(activity.requireActivity().applicationContext)
            .fragment(activity)
//            .activity(activity.activity)
            .configuration(locationConfiguration)
            .notify(this)
            .build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        locationManager!!.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        locationManager!!.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        locationManager!!.onDestroy()
    }

    fun getLocation() {
        if (locationManager != null) {
            locationManager!!.get()
        } else {
            throw IllegalStateException(
                "locationManager is null. "
                        + "Make sure you call super.initialize before attempting to getLocation"
            )
        }
    }

    override fun onProcessTypeChanged(@ProcessType processType: Int) {
        // override if needed
    }

    override fun onLocationFailed(type: Int) {
        mapsUtilListener.onLocationFailed(type)
    }

    override fun onLocationChanged(location: Location?) {
        mapsUtilListener.onLocationChanged(location)
    }

    override fun onPermissionGranted(alreadyHadPermission: Boolean) {
        // override if needed
    }

    override fun onStatusChanged(
        provider: String,
        status: Int,
        extras: Bundle
    ) {
        // override if needed
    }

    override fun onProviderEnabled(provider: String) {
        // override if needed
    }

    override fun onProviderDisabled(provider: String) {
        // override if needed
    }
}