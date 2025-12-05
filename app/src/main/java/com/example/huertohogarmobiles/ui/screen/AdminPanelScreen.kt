package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.ui.viewmodel.AdminViewModel

@Composable
fun AdminProductoCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Stock: ${producto.stock} | Precio: $${producto.precio} CLP", style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, "Eliminar")
                }
            }
        }
    }
}

@Composable
fun EstadisticasPanel(productos: List<Producto>) {
    Column(Modifier.padding(16.dp)) {
        Text("Estadísticas del Catálogo", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total de Productos: ${productos.size}")
        Text("Categorías Únicas: ${productos.distinctBy { it.categoria }.size}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var tabSeleccionada by remember { mutableStateOf(0) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin - ${uiState.username}") },
                actions = {
                    TextButton(onClick = {
                        viewModel.cerrarSesion()
                        onCerrarSesion()
                    }) {
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            if (tabSeleccionada == 0) {
                FloatingActionButton(onClick = onAgregarProducto) {
                    Icon(Icons.Default.Add, "Agregar Producto")
                }
            }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = tabSeleccionada) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = { tabSeleccionada = 0 },
                    text = { Text("Productos") }
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = { tabSeleccionada = 1 },
                    text = { Text("Estadísticas") }
                )
            }

            when (tabSeleccionada) {
                0 -> {
                    LazyColumn {
                        items(uiState.productos) { producto ->
                            AdminProductoCard(
                                producto = producto,
                                onEditar = { onEditarProducto(producto) },
                                onEliminar = { productoAEliminar = producto }
                            )
                        }
                    }
                }
                1 -> {
                    EstadisticasPanel(productos = uiState.productos)
                }
            }
        }
    }

    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar ${producto.nombre}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarProducto(producto)
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}