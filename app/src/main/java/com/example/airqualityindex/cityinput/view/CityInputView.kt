package com.example.airqualityindex.cityinput.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.airqualityindex.cityinput.viewmodel.CityInputState
import com.example.airqualityindex.cityinput.viewmodel.CityInputViewModel
import kotlinx.serialization.Serializable

@Serializable
object NavCityInputView

@Composable
fun CityInputScreen(
    viewModel: CityInputViewModel
) {
    val viewState by viewModel.state.collectAsState()
    val context = LocalContext.current
    when(viewState) {
        CityInputState.Loading -> {
            CircularProgressIndicator()
        }
        CityInputState.Idle, is CityInputState.Error -> {
            CityInputContent { cityName ->
                viewModel.fetchAQI(cityName)
            }

            if (viewState is CityInputState.Error) {
                val errorState = viewState as CityInputState.Error
                Toast.makeText(context, errorState.message, Toast.LENGTH_SHORT).show()
            }
        }
        is CityInputState.Success -> {

        }
    }
}

@Composable
fun CityInputContent(onSubmit: (String?) -> Unit) {
    var cityName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter a city name or leave blank for current location.")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter City Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSubmit(cityName) }) {
            Text("Get AQI")
        }
    }
}