package com.jetwiz.akudimana.repo

import android.content.Context
import android.location.Location
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.base.CST
import com.jetwiz.akudimana.domain.PlacesModel
import com.jetwiz.akudimana.network.ServiceFindPlace
import com.jetwiz.akudimana.util.U_Api
import timber.log.Timber
import wazma.punjabi.helper.U_Prefs

class RepoMap(var context: Context) {

    private var prefs: U_Prefs

    init {
        prefs = U_Prefs(context)
    }

    suspend fun findPlace(mLocation: Location):PlacesModel {
        val filterType = prefs.getPrefs().getString(CST.FILTER_TYPE_S, CST.DEF_TYPE)!!
        val radius = prefs.getPrefs().getInt(CST.RADIUS_I, CST.DEF_RADIUS)

        val lat = mLocation.latitude.toString()
        val long = mLocation.longitude.toString()

        val key = context.resources.getString(R.string.google_maps_key)

        val apiService = U_Api.retrofit.create(ServiceFindPlace::class.java)
        try {
            val res = apiService.findPlace("$lat,$long", "$radius", filterType,  key)
            if (res.results.isEmpty()) {
                return PlacesModel()
            } else {
                res.results.forEach {
                    Timber.d( "${it.name} ${it.types}")
                    Timber.d("long ${it.geometry!!.location!!.lng} - lat ${it.geometry!!.location!!.lat}")
                }
                return res
            }
        } catch (e:Throwable) {
            Timber.e(e)
            return PlacesModel()
        }
    }

    suspend fun nextPlace(pagetoken:String):PlacesModel {
        val key = context.resources.getString(R.string.google_maps_key)
        val apiService = U_Api.retrofit.create(ServiceFindPlace::class.java)
        try {
            val res = apiService.nextPlace(pagetoken, key)
            if (res.results.isEmpty()) {
                return PlacesModel()
            } else {
                Timber.d("<=======================================>")
                res.results.forEach {
                    Timber.d( "${it.name} ${it.types}")
                }
                return res
            }
        } catch (e:Throwable) {
            Timber.e(e)
            return PlacesModel()
        }
    }
}