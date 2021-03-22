package com.jetwiz.akudimana.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Southwest {
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lng")
    @Expose
    var lng: Double? = null
}