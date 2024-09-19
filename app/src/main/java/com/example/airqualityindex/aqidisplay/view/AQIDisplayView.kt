package com.example.airqualityindex.aqidisplay.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airqualityindex.aqidisplay.viewmodel.AQIDisplayState
import com.example.airqualityindex.aqidisplay.viewmodel.AQIDisplayViewModel
import com.example.airqualityindex.cityinput.model.AQIData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.CoroutineContext

@Serializable
data class NavAQIDisplay(
    val aqi: Int,
    val stationName: String
)

@Composable
fun AQIDisplayScreen(
    viewModel: AQIDisplayViewModel,
    navController: NavController
) {
    val viewState by viewModel.state.collectAsState()

    when(viewState) {
        AQIDisplayState.Loading -> {
            CircularProgressIndicator()
        }
        is AQIDisplayState.Display -> {
            val displayState = viewState as AQIDisplayState.Display
            AQIDisplayContent(aqi = displayState.aqi, stationName = displayState.stationName, navController = navController)
        }
        is AQIDisplayState.Error -> TODO()
    }
}

@Composable
fun AQIDisplayContent(aqi: Int, stationName: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AQI: $aqi")
        Text("City: $stationName")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigateUp()}) {
            Text("Back")
        }
    }
}