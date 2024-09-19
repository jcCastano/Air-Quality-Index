package com.example.airqualityindex.cityinput.viewmodel

import com.example.airqualityindex.cityinput.model.AQIData

sealed class CityInputState {
    object Idle : CityInputState()
    object Loading : CityInputState()
    data class Success(val aqiData: AQIData) : CityInputState()
    data class Error(val message: String) : CityInputState()
}
