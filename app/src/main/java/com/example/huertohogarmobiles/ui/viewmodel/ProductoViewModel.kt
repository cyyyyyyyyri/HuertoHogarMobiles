package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.ui.state.ProductoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repositorio: ProductoRepositoryImpl
) : ViewModel() {

    // 1. Estado para la LISTA de productos (Home)
    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    // 2. Estado para el producto INDIVIDUAL seleccionado (Detalle)
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    init {
        // Carga la lista general al iniciar
        cargarProductos()
    }

    /**
     * Carga la lista completa para el Home
     */
    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                estaCargando = true,
                error = null
            )

            repositorio.obtenerProductos()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = emptyList(),
                        error = exception.message ?: "Error desconocido al cargar productos"
                    )
                }
                .collect { productos ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = productos,
                        error = null
                    )
                }
        }
    }

    /**
     * Carga un producto específico por ID para la pantalla de Detalle.
     * Esta es la función que te faltaba.
     */
    fun cargarProductoPorId(id: Int) {
        viewModelScope.launch {
            // Reiniciamos el seleccionado para que no muestre el anterior mientras carga
            _productoSeleccionado.value = null
            val producto = repositorio.obtenerProductoPorId(id)
            _productoSeleccionado.value = producto
        }
    }

    // Funciones para el Administrador (suspend directas o launch)

    // Esta la mantienes por si la usas en otro lado como suspend
    suspend fun obtenerProductoPorId(id: Int) = repositorio.obtenerProductoPorId(id)

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.insertarProducto(producto)
        }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.actualizarProducto(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.eliminarProducto(producto)
        }
    }
}