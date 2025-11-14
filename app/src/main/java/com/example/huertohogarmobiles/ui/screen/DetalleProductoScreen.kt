package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.domain.model.Producto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    onBackClick: () -> Unit
) {
    // 1. Estados de la pantalla
    var producto: Producto? by remember { mutableStateOf(null) }
    var cantidadAComprar by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 2. Carga Asíncrona del Producto (SOLUCIÓN AL ERROR SUSPEND)
    LaunchedEffect(productoId) {
        // Ejecuta la suspend fun del repositorio dentro del scope de coroutine
        producto = productoRepository.obtenerProductoPorId(productoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        // 3. Renderizado Condicional: Muestra carga/error si el producto es nulo
        if (producto == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Uso de una variable local 'p' para el producto cargado (evita errores de smart cast)
        val p = producto!!

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del producto (usando Coil - AsyncImage)
            AsyncImage(
                model = p.imagenUrl,
                contentDescription = p.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Título y Categoría
                Text(p.nombre, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    p.categoria,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(Modifier.padding(vertical = 16.dp))

                // Precio y Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$${p.precio} CLP",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Stock: ${p.stock} disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (p.stock < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(Modifier.padding(vertical = 16.dp))

                // Descripción
                Text("Descripción", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(p.descripcion, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(32.dp))

                // Control de Cantidad y Botón de Agregar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Control de Cantidad
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (cantidadAComprar > 1) cantidadAComprar-- },
                            enabled = cantidadAComprar > 1
                        ) { Icon(Icons.Default.Remove, "Disminuir") }

                        Text(
                            "$cantidadAComprar",
                            Modifier.width(30.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                if (cantidadAComprar < p.stock) cantidadAComprar++
                            },
                            enabled = cantidadAComprar < p.stock
                        ) { Icon(Icons.Default.Add, "Aumentar") }
                    }

                    // Botón Agregar al Carrito
                    Button(
                        onClick = {
                            if (p.stock > 0 && cantidadAComprar > 0) {
                                scope.launch {
                                    carritoRepository.agregarProducto(p, cantidadAComprar)
                                    // Mostrar Snackbar de confirmación
                                    snackbarHostState.showSnackbar(
                                        message = "$cantidadAComprar ${p.nombre} añadido(s) al carrito.",
                                        actionLabel = "Ver Carrito",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        enabled = p.stock > 0 && cantidadAComprar > 0,
                        modifier = Modifier.weight(1f).padding(start = 16.dp)
                    ) {
                        Text("Añadir al Carrito")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Si el stock es 0, mostrar mensaje de agotado
                if (p.stock == 0) {
                    Text(
                        "¡AGOTADO!",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}