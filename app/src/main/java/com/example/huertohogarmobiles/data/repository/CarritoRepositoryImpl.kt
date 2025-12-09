package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity
import com.example.huertohogarmobiles.data.local.entity.toItemCarrito
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return dao.obtenerTodo().map { lista ->
            lista.map { it.toItemCarrito() }
        }
    }

    override fun obtenerTotal(): Flow<Double> {
        return dao.obtenerTotal().map { it ?: 0.0 }
    }

    override suspend fun agregarItem(item: ItemCarrito) {
        dao.insertar(item.toEntity())
    }

    override suspend fun actualizarCantidad(productoId: Int, cantidad: Int) {
        if (cantidad <= 0) {
            dao.eliminarProducto(productoId)
        } else {
            dao.actualizarCantidad(productoId, cantidad)
        }
    }

    override suspend fun eliminarItem(productoId: Int) {
        dao.eliminarProducto(productoId)
    }

    override suspend fun vaciarCarrito() {
        dao.vaciar()
    }
}