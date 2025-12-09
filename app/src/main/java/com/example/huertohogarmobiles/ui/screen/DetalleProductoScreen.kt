package com.example.huertohogarmobiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.huertohogarmobiles.ui.viewmodel.CarritoViewModel
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    onBackClick: () -> Unit,
    productoViewModel: ProductoViewModel = hiltViewModel(),
    carritoViewModel: CarritoViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar el producto
    LaunchedEffect(productoId) {
        productoViewModel.cargarProductoPorId(productoId)
    }

    // Ahora esto funcionará porque agregamos productoSeleccionado al ViewModel
    val producto by productoViewModel.productoSeleccionado.collectAsState()
    var cantidadAComprar by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // ... (El resto del código dentro del Scaffold está bien)
        producto?.let { p ->
            // ... contenido de la columna ...
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = p.imagenUrl,
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(p.nombre, style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        p.categoria,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(
                        Modifier.padding(vertical = 16.dp),
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
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
                    HorizontalDivider(
                        Modifier.padding(vertical = 16.dp),
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
                    Text("Descripción", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(p.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                                onClick = { if (cantidadAComprar < p.stock) cantidadAComprar++ },
                                enabled = cantidadAComprar < p.stock
                            ) { Icon(Icons.Default.Add, "Aumentar") }
                        }
                        Button(
                            onClick = {
                                if (p.stock > 0 && cantidadAComprar > 0) {
                                    scope.launch {
                                        carritoViewModel.agregarProducto(p, cantidadAComprar)
                                        snackbarHostState.showSnackbar("Producto añadido al carrito")
                                    }
                                }
                            },
                            enabled = p.stock > 0 && cantidadAComprar > 0,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            Text("Añadir al Carrito")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (p.stock == 0) {
                        Text(
                            "¡AGOTADO!",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}