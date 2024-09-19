package com.example.airqualityindex.cityinput.model.service

import android.media.session.MediaSession.Token
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AQIApiClient {

    private const val BASE_URL = "https://api.waqi.info/"
    private const val TOKEN = "3328e619c2457352f4fe9ac295e65ef58e1c1ed9"

    private val retrofit: Retrofit by lazy {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(TOKEN))
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: AQIApiService by lazy {
        retrofit.create(AQIApiService::class.java)
    }

}