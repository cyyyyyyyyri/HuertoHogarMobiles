package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.ui.state.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch // Manejo de errores en el Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


 // ProductoViewModel: Gestiona el estado de los productos (carga, éxito, error).


class ProductoViewModel(
    private val repositorio: ProductoRepositoryImpl
) : ViewModel() {


    private val _uiState = MutableStateFlow(ProductoUiState())


    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        // Cargar productos inmediatamente al crearse el ViewModel
        cargarProductos()
    }

    /**
     * carga la lista de productos desde el repositorio y actualiza el uistate.
     */
    fun cargarProductos() {
        viewModelScope.launch {
            // Indicar que está cargando
            _uiState.value = _uiState.value.copy(
                estaCargando = true,
                error = null
            )

            // 2. Observar Flow de Room y manejar resultados
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

    // Funciones para el Administrador

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

class ProductoViewModelFactory(
    private val repositorio: ProductoRepositoryImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que la clase sea ProductoViewModel
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}