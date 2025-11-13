package com.example.huertohogarmobiles.domain.model

/**
 * Modelo de dominio para Producto (HuertoHogar)
 * NO tiene anotaciones de Room. Incluye lógica de negocio.
 */
data class Producto(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: String,
    val stock: Int
) {
    /**
     * Formatea el precio al formato chileno (CLP) con separador de miles.
     * Ejemplo: 1200.0 -> "$1.200"
     */
    fun precioFormateado(): String {
        // Convierte el precio a formato entero para aplicar el separador de miles
        val precioEntero = precio.toLong()

        // Formateo manual: revierte, chunked(3) para separador, revierte
        val formato = precioEntero.toString().reversed().chunked(3).joinToString(".").reversed()

        return "$$formato CLP"
    }

    /**
     * Verifica si hay stock disponible.
     */
    val hayStock: Boolean
        get() = stock > 0
}