package com.example.huertohogarmobiles.ui.components

// 1. IMPORTS DE COMPOSE (UI)
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

// 2. IMPORTS DE GOOGLE MAPS MODEL (Clases de Latitud/Longitud)
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

// 3. IMPORTS DE MAPS COMPOSE (Componentes Composable)
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


val Santiago = LatLng(-33.4489, -70.6693)

@Composable
fun LocationPickerMap(
    modifier: Modifier = Modifier.fillMaxSize(),
    initialLocation: LatLng = Santiago,
    onLocationSelected: (LatLng) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    val markerPosition = remember(cameraPositionState.position.target) {
        cameraPositionState.position.target
    }

    onLocationSelected(markerPosition)

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        Marker(
            state = rememberMarkerState(position = markerPosition),
            title = "Punto de Envío",
            snippet = "Arrastra el mapa para seleccionar la ubicación",
            draggable = false
        )
    }
}