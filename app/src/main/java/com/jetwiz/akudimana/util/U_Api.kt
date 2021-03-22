package com.jetwiz.akudimana.util

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jetwiz.akudimana.base.CST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object U_Api {
    val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(CST.BASE_MAPS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val okHttpClient: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor()
            val clientBuilder = OkHttpClient.Builder()
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            clientBuilder.addInterceptor(logging)
            return clientBuilder.build()
        }
}
