package com.example.huertohogarmobiles.domain.repository

import com.example.huertohogarmobiles.domain.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun obtenerCarrito(): Flow<List<ItemCarrito>>
    fun obtenerTotal(): Flow<Double>
    suspend fun agregarItem(item: ItemCarrito)
    suspend fun actualizarCantidad(productoId: Int, cantidad: Int)
    suspend fun eliminarItem(productoId: Int)
    suspend fun vaciarCarrito()
}