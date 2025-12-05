package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.huertohogarmobiles.ui.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegistroViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var mostrarPassword by remember { mutableStateOf(false) }
    var mostrarConfirmarPassword by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegistroExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.formulario.nombreCompleto,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre Completo") },
                isError = uiState.errores.nombreCompletoError != null,
                supportingText = { MostrarError(uiState.errores.nombreCompletoError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formulario.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                isError = uiState.errores.emailError != null,
                supportingText = { MostrarError(uiState.errores.emailError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formulario.telefono,
                onValueChange = viewModel::onTelefonoChange,
                label = { Text("Teléfono") },
                isError = uiState.errores.telefonoError != null,
                supportingText = { MostrarError(uiState.errores.telefonoError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formulario.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección de Entrega") },
                isError = uiState.errores.direccionError != null,
                supportingText = { MostrarError(uiState.errores.direccionError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formulario.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { TogglePasswordIcon(mostrarPassword) { mostrarPassword = !mostrarPassword } },
                isError = uiState.errores.passwordError != null,
                supportingText = { MostrarError(uiState.errores.passwordError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.formulario.confirmarPassword,
                onValueChange = viewModel::onConfirmarPasswordChange,
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (mostrarConfirmarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { TogglePasswordIcon(mostrarConfirmarPassword) { mostrarConfirmarPassword = !mostrarConfirmarPassword } },
                isError = uiState.errores.confirmarPasswordError != null,
                supportingText = { MostrarError(uiState.errores.confirmarPasswordError) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.formulario.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptaTerminosChange
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    modifier = Modifier.clickable { viewModel.onAceptaTerminosChange(!uiState.formulario.aceptaTerminos) }
                )
            }
            MostrarError(uiState.errores.terminosError, Modifier.fillMaxWidth().padding(start = 16.dp))


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = viewModel::intentarRegistro,
                enabled = !uiState.estaRegistrando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.estaRegistrando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("REGISTRARME")
                }
            }

            if (uiState.errorGeneral != null) {
                Text(
                    uiState.errorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun MostrarError(mensaje: String?, modifier: Modifier = Modifier) {
    if (mensaje != null) {
        Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = modifier)
    } else {
        Spacer(modifier = modifier.height(20.dp))
    }
}

@Composable
private fun TogglePasswordIcon(isVisible: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña"
        )
    }
}