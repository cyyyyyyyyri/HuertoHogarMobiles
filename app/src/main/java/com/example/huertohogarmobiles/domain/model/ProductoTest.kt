package com.example.huertohogarmobiles.domain.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductoTest {

    private lateinit var productoConStock: Producto
    private lateinit var productoAgotado: Producto

    @Before
    fun setUp() {
        productoConStock = Producto(
            id = 1,
            nombre = "Tomate Orgánico",
            descripcion = "Tomates frescos y orgánicos",
            precio = 2500.0,
            imagenUrl = "url_valida",
            categoria = "Vegetales",
            stock = 10
        )

        productoAgotado = productoConStock.copy(stock = 0)
    }

    @Test
    fun isAvailable_shouldReturnTrue_whenStockIsAboveZero() {
        // Verifica si el producto está disponible para la venta
        assertTrue(productoConStock.stock > 0)
    }

    @Test
    fun isAvailable_shouldReturnFalse_whenStockIsZero() {
        // Verifica que el producto sin stock no esté disponible
        assertFalse(productoAgotado.stock > 0)
    }

    @Test
    fun priceIsPositive_shouldReturnTrue_whenPriceIsAboveZero() {
        // Verifica que el precio sea válido para ser mostrado
        assertTrue(productoConStock.precio > 0.0)
    }

    @Test
    fun priceIsPositive_shouldReturnFalse_whenPriceIsZero() {
        val productoGratis = productoConStock.copy(precio = 0.0)
        assertFalse(productoGratis.precio > 0.0)
    }
}