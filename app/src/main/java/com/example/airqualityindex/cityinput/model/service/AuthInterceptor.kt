package com.example.airqualityindex.cityinput.model.service

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlWithToken = originalRequest.url.newBuilder()
            .addQueryParameter("token", token)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithToken)
            .build()

        return chain.proceed(newRequest)
    }
}