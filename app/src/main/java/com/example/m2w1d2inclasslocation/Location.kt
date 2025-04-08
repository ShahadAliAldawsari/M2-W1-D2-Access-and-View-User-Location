package com.example.m2w1d2inclasslocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale















@Composable
fun GetCurrentLocation(updateMyLocationState: Boolean = true, context: Context = LocalContext.current) : String{

    val latitude = remember { mutableStateOf(0.0) }
    val longitude = remember { mutableStateOf(0.0) }

    //    val context = LocalContext.current
    var address by remember { mutableStateOf("Fetching address...") }

    // Create a FusedLocationProviderClient
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocationAndAddress(context, fusedLocationClient) {
                address = it ?: "Unable to fetch address"
            }
        } else {
            address = "Permission denied"
        }
    }

    // Check if permission is granted, otherwise request it
    LaunchedEffect(key1 = updateMyLocationState) {
        when (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionChecker.PERMISSION_GRANTED -> {
                fetchLocationAndAddress(context, fusedLocationClient) {
                    address = it ?: "Unable to fetch address"
                }
            }
            else -> locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // to see if it's updating
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
    }
//
//    // Display the address in a Composable
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start=16.dp, end = 16.dp, top=64.dp),
////        contentAlignment = Alignment.TopCenter
//    ) {
//        Text(text = "", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
//    }
    return address
}



@SuppressLint("MissingPermission")
private fun fetchLocationAndAddress(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onAddressFetched: (String?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            // Convert location to address
            getAddressFromLocation(context, location) { address ->
                onAddressFetched(address)
            }
        } ?: onAddressFetched("Location not found")
    }.addOnFailureListener {
        onAddressFetched("Error fetching location: ${it.message}")
    }
}

// Function to convert location to address using Geocoder
private fun getAddressFromLocation(
    context: Context,
    location: Location,
    onAddressFetched: (String?) -> Unit
) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val latitude = location.latitude
    val longitude = location.longitude

    try {
        val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                onAddressFetched(address)
            } else {
                onAddressFetched("No address found")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onAddressFetched("Error fetching address: ${e.message}")
    }
}