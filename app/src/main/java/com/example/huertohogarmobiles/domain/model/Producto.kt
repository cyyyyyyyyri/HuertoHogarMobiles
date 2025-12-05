package com.example.huertohogarmobiles.domain.model


data class Producto(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: String,
    val stock: Int
) {

    fun precioFormateado(): String {

        val precioEntero = precio.toLong()


        val formato = precioEntero.toString().reversed().chunked(3).joinToString(".").reversed()

        return "$$formato CLP"
    }

 // Verifica si hay stock
    val hayStock: Boolean
        get() = stock > 0
}