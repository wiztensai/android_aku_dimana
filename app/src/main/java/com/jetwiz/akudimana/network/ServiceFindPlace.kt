package com.jetwiz.akudimana.network

import com.jetwiz.akudimana.domain.PlacesModel
import retrofit2.http.*

interface ServiceFindPlace {
    @GET("place/nearbysearch/json")
    suspend fun findPlace(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types") types: String,
        @Query("key") key: String
    ): PlacesModel

    @GET("place/nearbysearch/json")
    suspend fun nextPlace(
        @Query("pagetoken") pagetoken: String,
        @Query("key") key: String
    ): PlacesModel
}
