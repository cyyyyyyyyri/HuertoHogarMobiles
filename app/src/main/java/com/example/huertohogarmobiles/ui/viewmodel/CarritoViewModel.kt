package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repositorio: CarritoRepository
) : ViewModel() {

    val carrito = repositorio.obtenerCarrito()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val total = repositorio.obtenerTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    fun agregarProducto(producto: Producto, cantidad: Int) {
        viewModelScope.launch {
            val item = ItemCarrito(producto, cantidad)
            repositorio.agregarItem(item)
        }
    }

    fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        viewModelScope.launch {
            repositorio.actualizarCantidad(productoId, nuevaCantidad)
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            repositorio.eliminarItem(id)
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            repositorio.vaciarCarrito()
        }
    }
}