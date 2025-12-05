package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val productos: List<Producto> = emptyList(),
    val username: String = ""
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val preferenciasManager: PreferenciasManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // CORRECCIÓN: El método se llama obtenerProductos, no obtenerProductosFlow
            productoRepository.obtenerProductos().collect { productos ->
                _uiState.update { currentState ->
                    currentState.copy(
                        productos = productos,
                        username = preferenciasManager.obtenerUsernameAdmin() ?: "Admin"
                    )
                }
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            productoRepository.eliminarProducto(producto)
        }
    }

    fun cerrarSesion() {
        preferenciasManager.cerrarSesionAdmin()
    }
}