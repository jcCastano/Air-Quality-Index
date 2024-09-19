package com.example.airqualityindex.aqidisplay.viewmodel

import androidx.lifecycle.ViewModel
import com.example.airqualityindex.cityinput.model.AQIData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AQIDisplayViewModel: ViewModel() {
    private val _state = MutableStateFlow<AQIDisplayState>(AQIDisplayState.Loading)
    val state: StateFlow<AQIDisplayState> = _state

    fun displayAQIData(aqi: Int, stationName: String) {
        _state.value = AQIDisplayState.Display(aqi, stationName)
    }
}