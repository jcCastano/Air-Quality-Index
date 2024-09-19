package com.example.airqualityindex.navigation

sealed class Screen(val route: String) {
    object CityInputScreen: Screen("city_input_screen")
    object AQIDisplayScreen: Screen("aqi_display_screen")
}