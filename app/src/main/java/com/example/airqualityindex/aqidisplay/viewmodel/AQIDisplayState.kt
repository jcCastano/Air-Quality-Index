package com.example.airqualityindex.aqidisplay.viewmodel

import com.example.airqualityindex.aqidisplay.model.AQIDisplayData
import com.example.airqualityindex.cityinput.model.AQIData

sealed class AQIDisplayState {
    object Loading: AQIDisplayState()
    data class Display(val aqiDisplayData: AQIDisplayData): AQIDisplayState()
    data class Error(val message: String): AQIDisplayState()
}