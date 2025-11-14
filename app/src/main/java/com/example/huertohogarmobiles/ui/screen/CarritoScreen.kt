package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import kotlinx.coroutines.launch

// Componente para un ítem individual del carrito
@Composable
fun CarritoItemCard(
    item: com.example.huertohogarmobiles.domain.model.ItemCarrito,
    onAumentarCantidad: () -> Unit,
    onDisminuirCantidad: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Precio: $${item.producto.precio} CLP", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Botón Disminuir
            IconButton(onClick = onDisminuirCantidad) {
                Icon(Icons.Default.Remove, "Disminuir")
            }
            // Cantidad
            Text("${item.cantidad}", Modifier.width(20.dp), textAlign = TextAlign.Center)
            // Botón Aumentar
            IconButton(onClick = onAumentarCantidad) {
                Icon(Icons.Default.Add, "Aumentar")
            }
            // Botón Eliminar
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, "Eliminar")
            }
        }
    }
    Divider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoRepository: CarritoRepository,
    onBackClick: () -> Unit
) {
    // Scope necesario para llamar suspend functions del repositorio
    val scope = rememberCoroutineScope()

    // Observar los Flows del repositorio
    val items by carritoRepository.obtenerCarrito().collectAsState(initial = emptyList())
    val total by carritoRepository.obtenerTotal().collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
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
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (items.isEmpty()) {
                // Carrito vacío
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío", Modifier.align(Alignment.Center))
                }
            } else {
                // Lista de items
                LazyColumn(Modifier.weight(1f)) {
                    items(items) { item ->
                        CarritoItemCard(
                            item = item,
                            onAumentarCantidad = {
                                scope.launch { // Ejecuta operación en el scope
                                    carritoRepository.modificarCantidad(item.producto.id, item.cantidad + 1)
                                }
                            },
                            onDisminuirCantidad = {
                                scope.launch {
                                    carritoRepository.modificarCantidad(item.producto.id, item.cantidad - 1)
                                }
                            },
                            onEliminar = {
                                scope.launch {
                                    carritoRepository.eliminarProducto(item.producto.id)
                                }
                            }
                        )
                    }
                }

                // Total y botón de compra (Fijo en la parte inferior)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Total: $${total.toInt()} CLP", // Mostrar total
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { /* Procesar compra */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("COMPRAR")
                        }
                    }
                }
            }
        }
    }
}