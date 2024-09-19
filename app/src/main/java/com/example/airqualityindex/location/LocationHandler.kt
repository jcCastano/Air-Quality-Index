package com.example.airqualityindex.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHandler(private val activity: ComponentActivity) {

    companion object {
        const val TAG = "LocationHandler"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private var locationCallback: ((Location?) -> Unit)? = null
    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(activity) }
    private val requestPermissionLauncher by lazy {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                fetchLocation()
            } else {
                Log.d(TAG, "Location permission denied")
            }
        }
    }

    // Public method to get current location
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        locationCallback = callback

        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Handle permission request result in the activity
    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            // Handle permission denied case
            locationCallback?.invoke(null)
        }
    }

    // Fetch the location from FusedLocationProviderClient
    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                locationCallback?.invoke(location)
            }
        }
    }
}