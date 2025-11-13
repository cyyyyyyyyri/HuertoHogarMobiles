package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.domain.model.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de productos (HuertoHogar)
 * Define las operaciones CRUD sin la implementación concreta (abstracción).
 */
interface RepositorioProductos {

    /**
     * Obtiene todos los productos como Flow (emite automáticamente los cambios de la BD).
     */
    fun obtenerProductos(): Flow<List<Producto>>

    /**
     * Obtiene un producto por su ID.
     */
    suspend fun obtenerProductoPorId(id: Int): Producto?

    /**
     * Inserta varios productos (para cargar datos iniciales).
     */
    suspend fun insertarProductos(productos: List<Producto>)

    /**
     * Inserta un solo producto.
     * Retorna el ID asignado (Long).
     */
    suspend fun insertarProducto(producto: Producto): Long

    /**
     * Actualiza un producto existente.
     */
    suspend fun actualizarProducto(producto: Producto)

    /**
     * Elimina un producto específico.
     */
    suspend fun eliminarProducto(producto: Producto)

    /**
     * Elimina todos los productos.
     */
    suspend fun eliminarTodosLosProductos()
}