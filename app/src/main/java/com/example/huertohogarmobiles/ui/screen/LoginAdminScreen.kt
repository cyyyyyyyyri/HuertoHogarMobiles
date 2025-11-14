package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdminScreen(
    onLoginExitoso: () -> Unit,
    onVolverClick: () -> Unit,
    // Callbacks provistos por NavGraph (usan PreferenciasManager)
    onValidarCredenciales: (String, String) -> Boolean,
    onGuardarSesion: (String) -> Unit
) {
    // Estados de los campos del formulario
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) } // null = sin error

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login Administrador") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Usuario
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    mensajeError = null // Limpiar error al escribir
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    mensajeError = null // Limpiar error al escribir
                },
                label = { Text("Contraseña") },
                // Ocultar caracteres
                visualTransformation = if (mostrarPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                // Ícono para mostrar/ocultar contraseña
                trailingIcon = {
                    IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                        Icon(
                            if (mostrarPassword) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            "Toggle password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Mostrar mensaje de error si existe
            mensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Ingreso
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        mensajeError = "Completa todos los campos"
                    } else if (onValidarCredenciales(username, password)) {
                        onGuardarSesion(username) // Guarda la sesión en PreferenciasManager
                        onLoginExitoso() // Navega al Panel Admin
                    } else {
                        mensajeError = "Credenciales incorrectas"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("INGRESAR")
            }
        }
    }
}