package com.example.airqualityindex.aqidisplay.model

import com.example.airqualityindex.cityinput.model.AQIData
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AQIDisplayData(
    val aqi: Int,
    val stationName: String,
    val currentForecast: PmForecast,
    val previousForecast: PmForecast,
    val futureForecast: PmForecast
) {
    companion object {
        val gson = Gson()

        fun fromJson(json: String): AQIDisplayData {
            return gson.fromJson(json, AQIDisplayData::class.java)
        }
    }

    fun toJson(): String {
        return gson.toJson(this)
    }

}

@Serializable
data class PmForecast(
    val pm25: Int?,
    val pm10: Int?
)

fun AQIData.toAQIDisplayData(): AQIDisplayData {

    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val yesterday = today.minusDays(1)

    val pm25ForecastsByDay = this.forecast.daily.pm25
        .filter { (it.day == today || it.day == tomorrow || it.day == yesterday) }
        .associateBy { it.day }
    val pm10ForecastsByDay = this.forecast.daily.pm10
        .filter { (it.day == today || it.day == tomorrow || it.day == yesterday) }
        .associateBy { it.day }

    val currentForecast = PmForecast(
        pm25 = pm25ForecastsByDay[today]?.avg,
        pm10 = pm10ForecastsByDay[today]?.avg
    )

    val futureForecast = PmForecast(
        pm25 = pm25ForecastsByDay[tomorrow]?.avg,
        pm10 = pm10ForecastsByDay[tomorrow]?.avg
    )

    val previousForecast = PmForecast(
        pm25 = pm25ForecastsByDay[yesterday]?.avg,
        pm10 = pm10ForecastsByDay[yesterday]?.avg
    )

    return AQIDisplayData(
        this.aqi,
        this.city.name,
        currentForecast,
        previousForecast,
        futureForecast
    )
}
