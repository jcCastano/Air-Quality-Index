package com.example.airqualityindex.aqidisplay.viewmodel

import com.example.airqualityindex.cityinput.model.AQIData

sealed class AQIDisplayState {
    object Loading: AQIDisplayState()
    data class Display(val aqi: Int, val stationName: String): AQIDisplayState()
    data class Error(val message: String): AQIDisplayState()
}