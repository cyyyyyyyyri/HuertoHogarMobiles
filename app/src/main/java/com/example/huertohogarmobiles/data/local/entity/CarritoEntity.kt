package com.example.huertohogarmobiles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.huertohogarmobiles.domain.model.ItemCarrito
import com.example.huertohogarmobiles.domain.model.Producto

/**
 * Entidad Room para tabla "carrito"
 * Representa un producto en el carrito del usuario.
 */
@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productoId: Int, // Referencia al producto original
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: String,
    val stock: Int,
    val cantidad: Int = 1 // Cantidad de este producto en el carrito
)

/**
 * Convierte CarritoEntity a Producto (Modelo de Dominio).
 */
fun CarritoEntity.toDomain(): Producto {
    return Producto(
        id = productoId,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagenUrl = imagenUrl,
        categoria = categoria,
        stock = stock
    )
}

/**
 * Convierte CarritoEntity a ItemCarrito (Modelo de Dominio con cantidad).
 * Esta es la función que te faltaba y causaba el error.
 */
fun CarritoEntity.toItemCarrito(): ItemCarrito {
    return ItemCarrito(
        producto = this.toDomain(), // Reutilizamos la conversión a Producto
        cantidad = this.cantidad
    )
}

/**
 * Convierte ItemCarrito a CarritoEntity (Para guardar en Base de Datos).
 * Esta función es necesaria para cuando haces 'insertar' en el repositorio.
 */
fun ItemCarrito.toEntity(): CarritoEntity {
    return CarritoEntity(
        productoId = producto.id,
        nombre = producto.nombre,
        descripcion = producto.descripcion,
        precio = producto.precio,
        imagenUrl = producto.imagenUrl,
        categoria = producto.categoria,
        stock = producto.stock,
        cantidad = cantidad
    )
}