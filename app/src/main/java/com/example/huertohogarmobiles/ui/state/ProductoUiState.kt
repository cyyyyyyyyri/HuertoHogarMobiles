package com.example.huertohogarmobiles.ui.state

import com.example.huertohogarmobiles.domain.model.Producto


data class ProductoUiState(
    val estaCargando: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null
) {
    // Helper: verifica si hay productos
    val hayProductos: Boolean
        get() = productos.isNotEmpty()
}