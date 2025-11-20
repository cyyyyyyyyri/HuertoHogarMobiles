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
import com.example.huertohogarmobiles.domain.model.Producto

// --- COMPONENTES AUXILIARES ---

// Tarjeta para mostrar un producto en la lista de administración
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
                // Botón Editar
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, "Editar")
                }
                // Botón Eliminar
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, "Eliminar")
                }
            }
        }
    }
}

// Panel para la pestaña de estadísticas
@Composable
fun EstadisticasPanel(productos: List<Producto>) {
    Column(Modifier.padding(16.dp)) {
        Text("Estadísticas del Catálogo", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total de Productos: ${productos.size}")
        Text("Categorías Únicas: ${productos.distinctBy { it.categoria }.size}")
        // Aquí puedes añadir métricas más avanzadas si las implementas
    }
}

// --- PANTALLA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    productos: List<Producto>,
    usernameAdmin: String,
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onEliminarProducto: (Producto) -> Unit,
    onCerrarSesion: () -> Unit // Callback para cerrar sesión
) {
    var tabSeleccionada by remember { mutableStateOf(0) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) } // Controla el estado del diálogo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin - $usernameAdmin") },
                actions = {
                    TextButton(onClick = onCerrarSesion) {
                        // ✅ CORRECCIÓN VISIBILIDAD: Usamos el color de Error para destacar la acción de salida.
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            // FAB visible solo en la pestaña de Productos (tab 0)
            if (tabSeleccionada == 0) {
                FloatingActionButton(onClick = onAgregarProducto) {
                    Icon(Icons.Default.Add, "Agregar Producto")
                }
            }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            // Pestañas (TabRow)
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

            // Contenido de las pestañas
            when (tabSeleccionada) {
                0 -> {
                    // Lista de productos
                    LazyColumn {
                        items(productos) { producto ->
                            AdminProductoCard(
                                producto = producto,
                                onEditar = { onEditarProducto(producto) },
                                onEliminar = { productoAEliminar = producto } // Abre el AlertDialog
                            )
                        }
                    }
                }
                1 -> {
                    // Panel de estadísticas
                    EstadisticasPanel(productos = productos)
                }
            }
        }
    }

    // AlertDialog para confirmar eliminación
    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar ${producto.nombre}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminarProducto(producto) // Llama al ViewModel para eliminar
                        productoAEliminar = null // Cierra el diálogo
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