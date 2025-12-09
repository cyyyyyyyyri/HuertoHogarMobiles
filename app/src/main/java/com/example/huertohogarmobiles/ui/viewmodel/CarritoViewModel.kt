package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repository: CarritoRepository
) : ViewModel() {

    // Convierte el Flow del repositorio en un StateFlow para que la UI lo observe eficientemente
    val carrito: StateFlow<List<ItemCarrito>> = repository.obtenerCarrito()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Observa el total del precio en tiempo real
    val total: StateFlow<Double> = repository.obtenerTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        viewModelScope.launch {
            repository.modificarCantidad(productoId, nuevaCantidad)
        }
    }

    fun eliminarProducto(productoId: Int) {
        viewModelScope.launch {
            repository.eliminarProducto(productoId)
        }
    }
}