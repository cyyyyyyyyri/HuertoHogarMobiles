package com.example.huertohogarmobiles.data.remote.api

import com.example.huertohogarmobiles.data.remote.dto.ProductListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {

    // Endpoint para obtener la lista de productos
    @GET("api/v1/productos")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ProductListResponse>
    // Se usa Response<T> para verificar el código HTTP (ej. 200, 404)
}