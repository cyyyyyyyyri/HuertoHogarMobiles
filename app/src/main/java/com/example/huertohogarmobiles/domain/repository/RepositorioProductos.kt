package com.example.huertohogarmobiles.domain.repository

import com.example.huertohogarmobiles.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface RepositorioProductos {


    fun obtenerProductos(): Flow<List<Producto>>

    suspend fun obtenerProductoPorId(id: Int): Producto?


    suspend fun insertarProductos(productos: List<Producto>)


    suspend fun insertarProducto(producto: Producto): Long


    suspend fun actualizarProducto(producto: Producto)


    suspend fun eliminarProducto(producto: Producto)


    suspend fun eliminarTodosLosProductos()
}