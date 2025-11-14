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

/**
 * ProductoViewModel: Gestiona el estado de los productos (carga, éxito, error).
 * Sobrevive a rotaciones de pantalla. [cite: 54, 143]
 */
class ProductoViewModel(
    private val repositorio: ProductoRepositoryImpl // Se inyecta la implementación concreta
) : ViewModel() {

    // _uiState: privado, solo el ViewModel puede modificarlo (MutableStateFlow)
    private val _uiState = MutableStateFlow(ProductoUiState())

    // uiState: público, solo lectura para la UI (StateFlow)
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        // Cargar productos inmediatamente al crearse el ViewModel [cite: 67]
        cargarProductos()
    }

    /**
     * Carga la lista de productos desde el repositorio y actualiza el UiState.
     */
    fun cargarProductos() {
        viewModelScope.launch {
            // 1. Indicar que está cargando (Usando copy para inmutabilidad)
            _uiState.value = _uiState.value.copy(
                estaCargando = true,
                error = null // Limpiar error si se está recargando
            )

            // 2. Observar Flow de Room y manejar resultados
            repositorio.obtenerProductos()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = emptyList(), // Vaciar lista en caso de error
                        error = exception.message ?: "Error desconocido al cargar productos"
                    )
                }
                .collect { productos -> // Bloque para recolectar datos (Éxito)
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = productos,
                        error = null // Limpiar error
                    )
                }
        }
    }

    // --- Funciones CRUD para el Administrador ---

    // La mayoría de estas funciones solo invocan al repositorio dentro de un viewModelScope.launch.

    /**
     * Busca un producto por ID.
     */
    suspend fun obtenerProductoPorId(id: Int) = repositorio.obtenerProductoPorId(id)

    /**
     * Agrega un nuevo producto (Admin).
     */
    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.insertarProducto(producto)
        }
    }

    /**
     * Actualiza un producto existente (Admin).
     */
    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.actualizarProducto(producto)
        }
    }

    /**
     * Elimina un producto (Admin).
     */
    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.eliminarProducto(producto)
        }
    }
}

/**
 * Factory: Crea instancias del ViewModel permitiendo inyectar el repositorio.
 * Esto es necesario ya que el ViewModel tiene parámetros en el constructor. [cite: 128, 146]
 */
class ProductoViewModelFactory(
    private val repositorio: ProductoRepositoryImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que la clase sea ProductoViewModel
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(repositorio) as T // Crea la instancia con el repositorio
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}