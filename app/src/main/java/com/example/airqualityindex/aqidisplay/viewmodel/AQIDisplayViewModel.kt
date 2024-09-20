package com.example.airqualityindex.aqidisplay.viewmodel

import androidx.lifecycle.ViewModel
import com.example.airqualityindex.aqidisplay.model.AQIDisplayData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AQIDisplayViewModel: ViewModel() {
    private val _state = MutableStateFlow<AQIDisplayState>(AQIDisplayState.Loading)
    val state: StateFlow<AQIDisplayState> = _state

    fun displayAQIData(aqiDisplayData: AQIDisplayData) {
        _state.value = AQIDisplayState.Display(aqiDisplayData)
    }
}