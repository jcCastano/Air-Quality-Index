package com.example.airqualityindex.cityinput.model

data class AQIResponse(
    val status: String,
    val data: AQIData
)

data class AQIData(
    val aqi: Int,
    val city: CityData,
    val forecast: Forecast
)

data class Forecast(
    val daily: Daily
)

data class Daily(
    val pm25: List<Pollutant>,
    val pm10: List<Pollutant>
)

data class Pollutant(
    val avg: Int,
    val day: String
)
