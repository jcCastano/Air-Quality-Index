package com.example.airqualityindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.airqualityindex.cityinput.model.service.AQIApiClient
import com.example.airqualityindex.cityinput.view.CityInputScreen
import com.example.airqualityindex.cityinput.viewmodel.CityInputViewModel
import com.example.airqualityindex.location.LocationHandler
import com.example.airqualityindex.navigation.AQIApp
import com.example.airqualityindex.ui.theme.AirQualityIndexTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationHandler = LocationHandler(this)
        setContent {
            AirQualityIndexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AQIApp(
                        apiService = AQIApiClient.service,
                        locationHandler = locationHandler
                    )
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AirQualityIndexTheme {
        Greeting("Android")
    }
}