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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.ui.viewmodel.CarritoViewModel


@Composable
fun CarritoItemCard(
    item: ItemCarrito,
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
            IconButton(onClick = onDisminuirCantidad) {
                Icon(Icons.Default.Remove, "Disminuir")
            }
            Text("${item.cantidad}", Modifier.width(20.dp), textAlign = TextAlign.Center)
            IconButton(onClick = onAumentarCantidad) {
                Icon(Icons.Default.Add, "Aumentar")
            }
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
    onBackClick: () -> Unit,
    viewModel: CarritoViewModel = hiltViewModel()
) {
    val items by viewModel.carrito.collectAsState()
    val total by viewModel.total.collectAsState()

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
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío", Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    items(items) { item ->
                        CarritoItemCard(
                            item = item,
                            onAumentarCantidad = { viewModel.modificarCantidad(item.producto.id, item.cantidad + 1) },
                            onDisminuirCantidad = { viewModel.modificarCantidad(item.producto.id, item.cantidad - 1) },
                            onEliminar = { viewModel.eliminarProducto(item.producto.id) }
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Total: $${total.toInt()} CLP",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { /* Lógica de compra */ },
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