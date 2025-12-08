package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity       // Asegúrate de tener este mapper
import com.example.huertohogarmobiles.data.local.entity.toItemCarrito   // Asegúrate de tener este mapper
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val carritoDao: CarritoDao
) : CarritoRepository {

    override fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerTodo().map { entities ->
            entities.map { it.toItemCarrito() }
        }
    }

    override fun obtenerTotal(): Flow<Double> {
        return carritoDao.obtenerTotal().map { total ->
            total ?: 0.0
        }
    }

    override suspend fun agregarItem(item: ItemCarrito) {
        carritoDao.insertar(item.toEntity())
    }

    override suspend fun actualizarCantidad(productoId: Int, cantidad: Int) {
        carritoDao.actualizarCantidad(productoId, cantidad)
    }

    override suspend fun eliminarItem(productoId: Int) {
        carritoDao.eliminarProducto(productoId)
    }

    override suspend fun vaciarCarrito() {
        carritoDao.vaciar()
    }
}