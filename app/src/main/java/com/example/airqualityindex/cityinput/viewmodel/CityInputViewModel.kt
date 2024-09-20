package com.example.airqualityindex.cityinput.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.airqualityindex.aqidisplay.model.toAQIDisplayData
import com.example.airqualityindex.aqidisplay.view.NavAQIDisplay
import com.example.airqualityindex.cityinput.model.service.AQIApiService
import com.example.airqualityindex.cityinput.model.service.AQIResult
import com.example.airqualityindex.location.LocationHandler
import com.example.airqualityindex.location.LocationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CityInputViewModel(
    private val apiService: AQIApiService,
    private val locationHandler: LocationHandler,
    private val navController: NavController
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
        locationHandler.getCurrentLocation { status, location, message ->
            when (status) {
                LocationStatus.SUCCESS -> {
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.d(TAG, "Lat: $latitude, Lon: $longitude")
                        viewModelScope.launch {
                            try {
                                val response = apiService.getAQIByLocation(latitude, longitude)
                                handleResponse(response)

                            } catch (e: Exception) {
                                Log.e(TAG, e.message ?: "Error fetching AQI by location")
                                _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = e.message ?: "Error fetching AQI by location")
                            }
                        }
                    } else {
                        Log.d(TAG, "Location not available")
                        _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = "Location not available")
                    }
                }
                LocationStatus.PERMISSION_DENIED -> {
                    _state.value = CityInputState.Error(CityInputErrorType.ALERT, title = "Permission Denied", message = "To enable location permissions go to: \n Settings > Location > Air Quality Index")
                }
                LocationStatus.ERROR -> {
                    _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = message)
                }
            }
        }
    }

    private fun fetchAQIByCity(cityName: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getAQIByCity(cityName)
                handleResponse(response)
            } catch (e: Exception) {
                _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = e.message ?: "Error fetching AQI by city name")
            }
        }
    }

    private fun handleResponse(response: Response<AQIResult>) {
        if (response.isSuccessful && response.body() != null) {
            response.body()?.let { body ->
                when(body) {
                    is AQIResult.Error -> _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = body.message)
                    is AQIResult.Success -> {
                        navController.navigate(NavAQIDisplay(body.data.toAQIDisplayData().toJson()))
                    }
                }

            }
        } else {
            _state.value = CityInputState.Error(CityInputErrorType.TOAST, message = "Failed to fetch AQI data")
        }
    }
    
}