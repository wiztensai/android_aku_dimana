package com.jetwiz.akudimana.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null

    @SerializedName("icon")
    @Expose
    var icon: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("photos")
    @Expose
    var photos: List<Photo>? = null

    @SerializedName("place_id")
    @Expose
    var placeId: String? = null

    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("scope")
    @Expose
    var scope: String? = null

    @SerializedName("types")
    @Expose
    var types: List<String>? = null

    @SerializedName("vicinity")
    @Expose
    var vicinity: String? = null

    @SerializedName("business_status")
    @Expose
    var businessStatus: String? = null

    @SerializedName("plus_code")
    @Expose
    var plusCode: PlusCode? = null

    @SerializedName("opening_hours")
    @Expose
    var openingHours: OpeningHours? = null

    @SerializedName("rating")
    @Expose
    var rating: Double? = null

    @SerializedName("user_ratings_total")
    @Expose
    var userRatingsTotal: Int? = null

    @SerializedName("permanently_closed")
    @Expose
    var permanentlyClosed: Boolean? = null
}