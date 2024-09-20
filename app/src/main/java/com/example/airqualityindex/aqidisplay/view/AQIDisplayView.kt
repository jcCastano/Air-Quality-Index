package com.example.airqualityindex.aqidisplay.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.airqualityindex.aqidisplay.model.AQIDisplayData
import com.example.airqualityindex.aqidisplay.model.GeoLocation
import com.example.airqualityindex.aqidisplay.model.PmForecast
import com.example.airqualityindex.aqidisplay.viewmodel.AQIDisplayState
import com.example.airqualityindex.aqidisplay.viewmodel.AQIDisplayViewModel
import kotlinx.serialization.Serializable

@Serializable
data class NavAQIDisplay(
    val aqiJson: String
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
            AQIDisplayContent(aqiData = displayState.aqiDisplayData, navController = navController)
        }
        is AQIDisplayState.Error -> TODO()
    }
}

@Composable
fun AQIDisplayContent(aqiData: AQIDisplayData, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AQIBox(aqiData = aqiData)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigateUp()}) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAQIDisplay() {
    val aqiDisplayData = AQIDisplayData(
        aqi = 15,
        stationName = "Philadelphia",
        geoLocation = GeoLocation(null, -75.126126),
        currentForecast = PmForecast(null, null),
        previousForecast = PmForecast(20, 25),
        futureForecast = PmForecast(30, 35)
    )
    AQIDisplayContent(aqiData = aqiDisplayData, navController = rememberNavController())
}

@Composable
fun AQIBox(aqiData: AQIDisplayData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("AQI: ${aqiData.aqi}", style = MaterialTheme.typography.displayLarge)
            Text("Station: ${aqiData.stationName}", style = MaterialTheme.typography.titleSmall)
            Text("Location: ${aqiData.geoLocation.latitude ?: "n/a"}, ${aqiData.geoLocation.longitude ?: "n/a"}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Row{
                ForecastItem(
                    modifier = Modifier.padding(end = 16.dp),
                    title = "-1 Day",
                    forecast = aqiData.previousForecast
                )
                ForecastItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Today",
                    forecast = aqiData.currentForecast
                )
                ForecastItem(
                    modifier = Modifier.padding(start = 16.dp),
                    title = "+1 Day",
                    forecast = aqiData.futureForecast
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ForecastItem(modifier: Modifier = Modifier, title: String, forecast: PmForecast?) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(text = "PM2.5 avg: ${forecast?.pm25 ?: "n/a"}", style = MaterialTheme.typography.labelSmall)
        Text(text = "PM10 avg: ${forecast?.pm10 ?: "n/a"}", style = MaterialTheme.typography.labelSmall)
    }
}

