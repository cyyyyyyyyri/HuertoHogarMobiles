package com.example.huertohogarmobiles.data.mapper

import com.example.huertohogarmobiles.data.local.entity.ProductoEntity
import com.example.huertohogarmobiles.data.remote.dto.ProductoDto

// DTO (API) -> Entity (Room)
fun ProductoDto.toEntity() = ProductoEntity(
    // Nota: Si tu ProductoEntity usa un ID autogenerado (id=0),
    // debes usar un campo para el ID de la API, como idApi.
    nombre = this.nombre,
    descripcion = "Descripción de API para ${this.nombre}", // Ajusta la descripción
    precio = this.precio,
    imagenUrl = this.imagenUrl,
    categoria = this.categoria,
    stock = this.stock
)