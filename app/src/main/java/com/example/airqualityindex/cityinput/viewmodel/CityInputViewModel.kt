package com.example.airqualityindex.cityinput.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airqualityindex.cityinput.model.service.AQIApiService
import com.example.airqualityindex.location.LocationHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class CityInputViewModel(
    private val apiService: AQIApiService,
    private val locationHandler: LocationHandler
): ViewModel() {

    private val _state = MutableStateFlow<CityInputState>(CityInputState.Idle)
    val state: StateFlow<CityInputState> = _state

    companion object {
        private const val TAG = "CityInputViewModel"
    }

    fun fetchAQI(cityName: String?) {
        _state.value = CityInputState.Loading
        if (cityName.isNullOrEmpty()) {
            fetchAQIByLocation()
        } else {
            fetchAQIByCity(cityName)
        }
    }

    private fun fetchAQIByLocation() {
        locationHandler.getCurrentLocation { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d(TAG, "Lat: $latitude, Lon: $longitude")
                viewModelScope.launch {
                    try {
                        val response = apiService.getAQIByLocation(latitude, longitude)
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.let { body ->
                                _state.value = CityInputState.Success(body.data)
                            }
                        } else {
                            _state.value = CityInputState.Error("Failed to fetch AQI data")
                        }
                    } catch (e: Exception) {
                        _state.value = CityInputState.Error(e.message ?: "Error fetching AQI by location")
                    }
                }
            } else {
                Log.d(TAG, "Location not available")
                _state.value = CityInputState.Error("Location not available")
            }
        }
    }

    private fun fetchAQIByCity(cityName: String) {

    }
    
}