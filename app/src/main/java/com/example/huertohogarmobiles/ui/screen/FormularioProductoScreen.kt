package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.domain.model.Producto
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int,
    productoRepository: ProductoRepositoryImpl,
    onBackClick: () -> Unit
) {
    // Necesario para llamar funciones suspend (asíncronas)
    val scope = rememberCoroutineScope()

    // 1. Lógica Inicial
    val esEdicion = productoId > 0
    val tituloPantalla = if (esEdicion) "Editar Producto" else "Nuevo Producto"

    // 2. Carga Inicial del Producto para Edición
    var productoOriginal: Producto? = null

    // Si es edición, cargamos el producto usando runBlocking (para el estado inicial)
    if (esEdicion) {
        productoOriginal = remember(productoId) {
            // runBlocking se usa aquí solo para inicializar el estado mutable
            runBlocking { productoRepository.obtenerProductoPorId(productoId) }
        }
    }

    // 3. Estado local del formulario (inicializado con el producto original o vacío)
    var nombre by remember { mutableStateOf(productoOriginal?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(productoOriginal?.descripcion ?: "") }
    var precio by remember { mutableStateOf(productoOriginal?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(productoOriginal?.stock?.toString() ?: "") } // <-- ESTADO STOCK
    var categoria by remember { mutableStateOf(productoOriginal?.categoria ?: "") }
    var imagenUrl by remember { mutableStateOf(productoOriginal?.imagenUrl ?: "") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloPantalla) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Fila para Precio y Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo Precio (CORREGIDO para un solo punto decimal)
                OutlinedTextField(
                    value = precio,
                    onValueChange = { nuevoValor ->
                        // Filtra para aceptar solo dígitos y un único punto decimal
                        val filtered = nuevoValor.filter { char -> char.isDigit() || char == '.' }

                        // Solo actualiza si no hay más de un punto decimal
                        if (filtered.count { it == '.' } <= 1) {
                            precio = filtered
                        }
                    },
                    label = { Text("Precio (CLP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                // ✅ CAMPO STOCK AÑADIDO Y CORREGIDO
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it.filter { it.isDigit() } }, // Solo permite números
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Categoría
            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo URL de Imagen
            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de Imagen") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botón Principal (Guardar/Crear)
            Button(
                onClick = {
                    scope.launch {
                        // 1. Recolectar datos y corregir tipos
                        val productoAGuardar = (productoOriginal ?: Producto(
                            id = 0,
                            nombre = "",
                            descripcion = "",
                            precio = 0.0,
                            stock = 0,
                            categoria = "",
                            imagenUrl = ""
                        )).copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toDoubleOrNull() ?: 0.0, // Conversión segura a Double
                            stock = stock.toIntOrNull() ?: 0,         // Conversión segura a Int
                            categoria = categoria,
                            imagenUrl = imagenUrl
                        )

                        // 2. Llamar al repositorio
                        if (esEdicion) {
                            productoRepository.actualizarProducto(productoAGuardar)
                        } else {
                            productoRepository.insertarProducto(productoAGuardar)
                        }

                        // 3. Volver a la pantalla anterior
                        onBackClick()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Text(if (esEdicion) "GUARDAR CAMBIOS" else "CREAR PRODUCTO")
            }

            // Botón de Cancelar
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}
