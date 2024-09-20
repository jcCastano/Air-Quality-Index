package com.example.airqualityindex.cityinput.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.window.DialogProperties
import com.example.airqualityindex.cityinput.viewmodel.CityInputErrorType
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        CityInputState.Idle, is CityInputState.Error -> {
            CityInputContent { cityName ->
                viewModel.fetchAQI(cityName)
            }

            if (viewState is CityInputState.Error) {
                val errorState = viewState as CityInputState.Error

                when (errorState.type) {
                    CityInputErrorType.TOAST -> {
                        Toast.makeText(context, errorState.message, Toast.LENGTH_SHORT).show()
                    }
                    CityInputErrorType.ALERT -> {
                        ShowAlertDialog(
                            title = errorState.title,
                            message = errorState.message,
                            showAlert = true
                        )
                    }
                }
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

@Composable
fun ShowAlertDialog(title: String, message: String, showAlert: Boolean) {
    var showDialog by remember { mutableStateOf(showAlert) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text(text = "Dismiss")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}