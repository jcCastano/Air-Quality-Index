package com.example.airqualityindex.cityinput.model.service

import com.example.airqualityindex.cityinput.model.AQIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AQIApiService {
    @GET("feed/geo:{lat};{lon}/")
    suspend fun getAQIByLocation(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double
    ): Response<AQIResponse>
}