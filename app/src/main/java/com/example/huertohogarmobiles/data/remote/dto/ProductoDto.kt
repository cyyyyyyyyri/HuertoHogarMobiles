package com.example.huertohogarmobiles.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductoDto(
    // Ejemplo de mapeo si la API usa snake_case (product_id)
    @Json(name = "product_id")
    val idApi: String,

    @Json(name = "product_name")
    val nombre: String,

    @Json(name = "price_clp")
    val precio: Double,

    @Json(name = "image_url")
    val imagenUrl: String,

    @Json(name = "stock_quantity")
    val stock: Int,

    @Json(name = "category_name")
    val categoria: String,

    // Campos añadidos para la funcionalidad del mapa
    @Json(name = "latitude")
    val latitud: Double,

    @Json(name = "longitude")
    val longitud: Double
)

// Si tu API devuelve un objeto contenedor con una lista de productos
@JsonClass(generateAdapter = true)
data class ProductListResponse(
    @Json(name = "products")
    val products: List<ProductoDto>,
    @Json(name = "total_count")
    val totalCount: Int
)