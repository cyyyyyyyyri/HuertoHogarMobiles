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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int,
    productoRepository: ProductoRepositoryImpl,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val esEdicion = productoId > 0
    val tituloPantalla = if (esEdicion) "Editar Producto" else "Nuevo Producto"

    // Estado local del formulario, inicializado vacío
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    // Carga asíncrona del producto para edición usando LaunchedEffect
    LaunchedEffect(key1 = productoId) {
        if (esEdicion) {
            productoRepository.obtenerProductoPorId(productoId)?.let { producto ->
                nombre = producto.nombre
                descripcion = producto.descripcion
                precio = producto.precio.toString()
                stock = producto.stock.toString()
                categoria = producto.categoria
                imagenUrl = producto.imagenUrl
            }
        }
    }

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
                // Campo Precio
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
                    // ✅ CAMBIO CLAVE: Usamos KeyboardType.Number para compatibilidad
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
                        val productoAGuardar = Producto(
                            id = if (esEdicion) productoId else 0,
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            stock = stock.toIntOrNull() ?: 0,
                            categoria = categoria,
                            imagenUrl = imagenUrl
                        )

                        if (esEdicion) {
                            productoRepository.actualizarProducto(productoAGuardar)
                        } else {
                            productoRepository.insertarProducto(productoAGuardar)
                        }

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
