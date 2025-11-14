package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PortadaScreen(
    onEntrarClick: () -> Unit, // Callback para navegar a Home
    onAdminClick: () -> Unit   // Callback para navegar al Login Admin
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono grande
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Logo StingCommerce",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "HUERTO HOGAR", // Usando el nombre de tu proyecto
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón principal (Entrar a la tienda)
            Button(
                onClick = onEntrarClick, // Llama al callback de navegación
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("ENTRAR A LA TIENDA", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón secundario (admin)
            OutlinedButton(
                onClick = onAdminClick, // Llama al callback de navegación
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acceso Administrador")
            }
        }
    }
}