package com.example.airqualityindex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.airqualityindex.aqidisplay.view.AQIDisplayScreen
import com.example.airqualityindex.aqidisplay.view.NavAQIDisplay
import com.example.airqualityindex.aqidisplay.viewmodel.AQIDisplayViewModel
import com.example.airqualityindex.cityinput.model.service.AQIApiClient
import com.example.airqualityindex.cityinput.model.service.AQIApiService
import com.example.airqualityindex.cityinput.view.CityInputScreen
import com.example.airqualityindex.cityinput.view.NavCityInputView
import com.example.airqualityindex.cityinput.viewmodel.CityInputViewModel
import com.example.airqualityindex.location.LocationHandler

@Composable
fun AQIApp(apiService: AQIApiService, locationHandler: LocationHandler) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavCityInputView) {
        composable<NavCityInputView> {
            CityInputScreen(
                viewModel = CityInputViewModel(apiService, locationHandler, navController)
            )
        }
        composable<NavAQIDisplay> {backStackEntry ->
            val args = backStackEntry.toRoute<NavAQIDisplay>()
            val viewModel = AQIDisplayViewModel()
            viewModel.displayAQIData(args.aqi, args.stationName)
            AQIDisplayScreen(viewModel = viewModel, navController = navController)
        }
    }
}