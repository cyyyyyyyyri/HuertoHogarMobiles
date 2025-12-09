package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
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

    val carrito = repository.obtenerCarrito()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val total = repository.obtenerTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        viewModelScope.launch {
            repository.actualizarCantidad(productoId, nuevaCantidad)
        }
    }

    fun eliminarProducto(productoId: Int) {
        viewModelScope.launch {
            repository.eliminarItem(productoId)
        }
    }
}