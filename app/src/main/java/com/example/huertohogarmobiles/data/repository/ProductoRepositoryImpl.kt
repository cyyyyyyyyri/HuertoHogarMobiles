package com.example.huertohogarmobiles.data.repository

import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity
import com.example.huertohogarmobiles.data.local.entity.toProducto
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del repositorio de productos (HuertoHogar)
 * Traduce entre entidades Room y modelos del dominio.
 */
class ProductoRepositoryImpl(
    private val productoDao: ProductoDao // Inyección del DAO de Room
) : RepositorioProductos {

    // ----------------- READ Operations -----------------

    override fun obtenerProductos(): Flow<List<Producto>> {
        // Convierte Flow<List<ProductoEntity>> a Flow<List<Producto>>
        return productoDao.obtenerTodosLosProductos()
            .map { entities ->
                entities.map { it.toProducto() } // Mapea cada entidad a modelo
            }
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? {
        // Obtiene la entidad y la mapea a modelo (o retorna null)
        return productoDao.obtenerProductoPorId(id)?.toProducto()
    }

    // ----------------- WRITE Operations -----------------

    override suspend fun insertarProductos(productos: List<Producto>) {
        val entities = productos.map { it.toEntity() }
        productoDao.insertarProductos(entities)
    }

    override suspend fun insertarProducto(producto: Producto): Long {
        return productoDao.insertarProducto(producto.toEntity())
    }

    override suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizarProducto(producto.toEntity())
    }

    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto.toEntity())
    }

    override suspend fun eliminarTodosLosProductos() {
        productoDao.eliminarTodosLosProductos()
    }
}