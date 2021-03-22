package com.jetwiz.akudimana.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlacesModel {
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>? = null

    @SerializedName("next_page_token")
    @Expose
    var nextPageToken: String? = null

    @SerializedName("results")
    @Expose
    var results: MutableList<Result> = mutableListOf()

    @SerializedName("status")
    @Expose
    var status: String? = null
}