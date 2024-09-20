package com.example.airqualityindex.cityinput.model.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AQIApiService {
    @GET("feed/geo:{lat};{lon}/")
    suspend fun getAQIByLocation(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double
    ): Response<AQIResult>

    @GET("feed/{city}/")
    suspend fun getAQIByCity(
        @Path("city") city: String
    ): Response<AQIResult>
}