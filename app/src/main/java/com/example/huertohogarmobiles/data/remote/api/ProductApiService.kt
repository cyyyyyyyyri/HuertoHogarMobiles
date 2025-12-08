package com.example.huertohogarmobiles.data.remote.api

import com.example.huertohogarmobiles.data.remote.dto.ProductListResponse
import com.example.huertohogarmobiles.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("api/v1/productos")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ProductListResponse>

    @GET("api/v1/productos/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductoDto>

    @POST("api/v1/productos")
    suspend fun createProduct(@Body product: ProductoDto): Response<ProductoDto>

    @PUT("api/v1/productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: ProductoDto): Response<ProductoDto>

    @DELETE("api/v1/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}