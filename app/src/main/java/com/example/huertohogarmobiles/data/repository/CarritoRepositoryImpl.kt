package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity // Asegúrate de tener este mapper o import
import com.example.huertohogarmobiles.data.local.entity.toItemCarrito // Asegúrate de tener este mapper o import
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val carritoDao: CarritoDao
) : CarritoRepository {

    override fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerCarrito().map { entities ->
            entities.map { it.toItemCarrito() }
        }
    }

    override suspend fun agregarItem(item: ItemCarrito) {
        // Asumiendo que tu DAO tiene un método insertar o agregar
        carritoDao.insertarItem(item.toEntity())
    }

    override suspend fun actualizarCantidad(item: ItemCarrito, cantidad: Int) {
        // Puedes implementar lógica aquí o llamar al DAO
        val itemActualizado = item.copy(cantidad = cantidad)
        carritoDao.actualizarItem(itemActualizado.toEntity())
    }

    override suspend fun eliminarItem(item: ItemCarrito) {
        carritoDao.eliminarItem(item.toEntity())
    }

    override suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }
}