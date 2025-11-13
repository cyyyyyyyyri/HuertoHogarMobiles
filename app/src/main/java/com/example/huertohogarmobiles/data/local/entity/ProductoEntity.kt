package com.example.huertohogarmobiles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.huertohogarmobiles.domain.model.Producto

/**
 * Entidad Room para productos (HuertoHogar)
 * Representa la tabla "productos" en la base de datos SQLite.
 */
@Entity(tableName = "productos")
data class ProductoEntity(
    // Clave primaria autogenerada (ID de la tabla)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double, // Precio en CLP
    val imagenUrl: String, // URL para Coil
    val categoria: String, // Ej: Frutas Frescas, Verduras Orgánicas
    val stock: Int // Disponibilidad
)

/**
 * Función de extensión: Convierte la entidad de base de datos (ProductoEntity)
 * al modelo de dominio (Producto).
 */
fun ProductoEntity.toProducto() = Producto(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = categoria,
    stock = stock
)

/**
 * Función de extensión: Convierte el modelo del dominio (Producto)
 * a entidad de base de datos (ProductoEntity).
 */
fun Producto.toEntity() = ProductoEntity(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = categoria,
    stock = stock
)