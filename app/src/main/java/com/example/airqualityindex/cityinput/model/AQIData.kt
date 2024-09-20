package com.example.airqualityindex.cityinput.model

import java.time.LocalDate
import java.util.Date

data class AQIResponse(
    val status: String,
    val data: AQIData
)

data class AQIData(
    val aqi: Int,
    val city: CityData,
    val forecast: Forecast
)

data class CityData(
    val name: String,
    val geo: List<Double>
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
    val day: LocalDate
)
