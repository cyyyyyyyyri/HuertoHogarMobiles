package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModel
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModelFactory


@Composable
fun ProductoCard(producto: com.example.huertohogarmobiles.domain.model.Producto, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onClick(producto.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("$${producto.precio} CLP / Kilo", style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = "Ver Detalle")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    onProductoClick: (Int) -> Unit,
    onCarritoClick: () -> Unit,
    onRegistroClick: () -> Unit,
    onVolverPortada: () -> Unit
) {
    // 1. Obtener/Crear ViewModel
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(productoRepository)
    )

    // 2. Observar el estado de la UI (Flow -> State)
    val uiState by viewModel.uiState.collectAsState()

    // 3. Estados locales para filtros
    var textoBusqueda by remember { mutableStateOf("") }
    val categoriaSeleccionada by remember { mutableStateOf<String?>(null) } // Variable de estado no usada en el filtro simple

    // 4. Lógica de Filtrado
    val productosFiltrados = remember(uiState.productos, textoBusqueda, categoriaSeleccionada) {
        uiState.productos.filter { producto ->
            val coincideTexto = textoBusqueda.isBlank() ||
                    producto.nombre.contains(textoBusqueda, ignoreCase = true)

            val coincideCategoria = categoriaSeleccionada == null ||
                    producto.categoria == categoriaSeleccionada // Asumiendo que producto tiene una propiedad 'categoria'

            coincideTexto && coincideCategoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos HuertoHogar") },
                navigationIcon = {
                    IconButton(onClick = onVolverPortada) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onRegistroClick) {
                        Icon(Icons.Default.Person, "Registro")
                    }
                    IconButton(onClick = onCarritoClick) {
                        Icon(Icons.Default.ShoppingCart, "Carrito")
                    }
                }
            )
        }
    ) { paddingValues ->
        when { // Manejo de estados (Cargando, Error, Éxito)
            uiState.estaCargando -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}")
                }
            }
            else -> {
                Column(Modifier.padding(paddingValues)) {
                    // Campo de búsqueda
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        placeholder = { Text("Buscar productos...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) }
                    )

                    // Lista de productos
                    LazyColumn {
                        items(productosFiltrados) { producto ->
                            ProductoCard(
                                producto = producto,
                                onClick = { onProductoClick(producto.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}