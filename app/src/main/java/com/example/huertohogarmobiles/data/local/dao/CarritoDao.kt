package com.example.huertohogarmobiles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import kotlinx.coroutines.flow.Flow

// Operaciones para carrito en la bd
@Dao
interface CarritoDao {

    @Query("SELECT * FROM carrito")
    fun obtenerTodo(): Flow<List<CarritoEntity>>


    @Insert
    suspend fun insertar(item: CarritoEntity)


    @Query("DELETE FROM carrito")
    suspend fun vaciar()

    @Query("SELECT SUM(precio * cantidad) FROM carrito")
    fun obtenerTotal(): Flow<Double?>


    @Query("SELECT * FROM carrito WHERE productoId = :productoId LIMIT 1")
    suspend fun obtenerPorProductoId(productoId: Int): CarritoEntity?


    @Query("UPDATE carrito SET cantidad = :cantidad WHERE productoId = :productoId")
    suspend fun actualizarCantidad(productoId: Int, cantidad: Int)


    @Query("DELETE FROM carrito WHERE productoId = :productoId")
    suspend fun eliminarProducto(productoId: Int)
}