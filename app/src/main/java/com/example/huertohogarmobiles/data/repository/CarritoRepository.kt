package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import com.example.huertohogarmobiles.data.local.entity.toDomain
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository del carrito - Gestiona la lógica de añadir, modificar y obtener items.
 */
class CarritoRepository(private val carritoDao: CarritoDao) {

    /**
     * Obtiene todos los items del carrito, mapeados a ItemCarrito (incluye cantidad).
     */
    fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerTodo()
            .map { entities ->
                entities.map { entity ->
                    ItemCarrito(
                        producto = entity.toDomain(), // Mapea a Producto
                        cantidad = entity.cantidad
                    )
                }
            }
    }

    /**
     * Agrega o incrementa la cantidad de un producto.
     */
    suspend fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        // Paso 1: Buscar si ya existe
        val existente = carritoDao.obtenerPorProductoId(producto.id)

        if (existente != null) {
            // Caso A: Ya existe -> sumar cantidad
            val nuevaCantidad = existente.cantidad + cantidad
            carritoDao.actualizarCantidad(producto.id, nuevaCantidad)
        } else {
            // Caso B: No existe -> insertar nuevo item
            val entity = CarritoEntity(
                productoId = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                imagenUrl = producto.imagenUrl,
                categoria = producto.categoria,
                stock = producto.stock,
                cantidad = cantidad // Inicia con la cantidad especificada
            )
            carritoDao.insertar(entity)
        }
    }

    /**
     * Modifica la cantidad de un producto ya en el carrito.
     * Si la nueva cantidad es <= 0, elimina el item.
     */
    suspend fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(productoId)
        } else {
            carritoDao.actualizarCantidad(productoId, nuevaCantidad)
        }
    }

    /**
     * Elimina un producto específico del carrito por su ID de producto.
     */
    suspend fun eliminarProducto(productoId: Int) {
        carritoDao.eliminarProducto(productoId)
    }

    /**
     * Vacía el carrito completo.
     */
    suspend fun vaciarCarrito() {
        carritoDao.vaciar()
    }

    /**
     * Obtiene el precio total del carrito (retorna 0.0 si está vacío).
     */
    fun obtenerTotal(): Flow<Double> {
        return carritoDao.obtenerTotal()
            .map { it ?: 0.0 } // Convierte null (carrito vacío) a 0.0
    }
}