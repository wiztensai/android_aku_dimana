package com.jetwiz.akudimana.domain

import android.location.Location
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Geometry {
    @SerializedName("location")
    @Expose
    var location: com.jetwiz.akudimana.domain.Location? = null

    @SerializedName("viewport")
    @Expose
    var viewport: Viewport? = null
}