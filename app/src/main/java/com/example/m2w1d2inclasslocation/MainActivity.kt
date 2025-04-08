package com.example.m2w1d2inclasslocation

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.m2w1d2inclasslocation.ui.theme.M2W1D2InClassLocationTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            M2W1D2InClassLocationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ScreenContent()
//                        BottomCard()
//                        GetCurrentLocation() //Check above code for this
                    }
                }
            }
        }
    }
}


@Composable
fun MapScreen() {
    val location = LatLng(0.0, 0.0) // I need to make it dynamic
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    )
}



@Composable
fun ScreenContent() {

//    val systemUiController = rememberSystemUiController()

    // Hide system UI for fullscreen effect
//    systemUiController.isSystemBarsVisible = false
    var updateMyLocationState by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
//        // Background image filling the screen
//        Image(
//            painter = painterResource(id = imageRes),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )

    }

    // Get screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val offsetY =
        with(LocalDensity.current) { (screenHeight / 2.8f).toPx().toInt() } // Convert Dp to pixels

    Box(modifier = Modifier.fillMaxSize()) {
        // Card positioned half from the bottom
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
//                .offset { IntOffset(x = 0, y = offsetY) } // Offset using calculated pixel value
                .wrapContentSize(), // Adjust card width as needed
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {

            Column(
                modifier = Modifier
//                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 64.dp),
//                contentAlignment = Alignment.TopCenter
            ) {
                Text("The current Location is:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(GetCurrentLocation(updateMyLocationState))
            }

            Spacer(Modifier.height(24.dp))

            val context = LocalContext.current
            // Card content
            Button(
                onClick = {
                    updateMyLocationState = !updateMyLocationState
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Update My Location",
                    fontSize = 16.sp,
                    modifier = Modifier
//                        .fillMaxSize()
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }
    }
}