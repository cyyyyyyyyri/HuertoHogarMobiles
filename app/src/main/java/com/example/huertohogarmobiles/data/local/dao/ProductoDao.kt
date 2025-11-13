package com.example.huertohogarmobiles.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.huertohogarmobiles.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO de productos (HuertoHogar)
 * Define las operaciones de base de datos para la tabla 'productos'.
 */
@Dao
interface ProductoDao {

    /**
     * Obtiene todos los productos ordenados por nombre.
     * Devuelve un Flow (observable) que se actualiza automáticamente.
     */
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntity>>

    /**
     * Obtiene un producto por su ID.
     */
    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    /**
     * Inserta varios productos (útil para cargar datos iniciales).
     * Si ya existen, los reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductoEntity>)

    /**
     * Inserta un solo producto.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: ProductoEntity): Long

    /**
     * Actualiza un producto existente.
     */
    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)

    /**
     * Elimina un producto específico.
     */
    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)

    /**
     * Elimina todos los productos.
     */
    @Query("DELETE FROM productos")
    suspend fun eliminarTodosLosProductos()
}