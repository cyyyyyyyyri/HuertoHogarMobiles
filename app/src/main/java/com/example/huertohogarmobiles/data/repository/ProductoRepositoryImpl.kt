package com.example.huertohogarmobiles.data.repository
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity
import com.example.huertohogarmobiles.data.local.entity.toProducto
import com.example.huertohogarmobiles.data.mapper.toEntity
import com.example.huertohogarmobiles.data.remote.api.ProductApiService
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import android.util.Log //



class ProductoRepositoryImpl @Inject constructor(
    private val productoDao: ProductoDao,
    private val apiService: ProductApiService,
    private val ioDispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO
) : RepositorioProductos {


    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductos()
            .map { entities ->
                entities.map { it.toProducto() }
            }
    }


    suspend fun refreshProducts() {
        withContext(ioDispatcher) {
            try {
                val response = apiService.getProducts()

                if (response.isSuccessful) {
                    val productsDto = response.body()
                    productsDto?.products?.let { dtos ->


                        val entities = dtos.map { it.toEntity() }


                        productoDao.eliminarTodosLosProductos()
                        productoDao.insertarProductos(entities)
                    }
                } else {
                    Log.e("ProductoRepo", "Error HTTP al refrescar: ${response.code()}")

                }
            } catch (e: Exception) {

                Log.e("ProductoRepo", "Error de Red/IO: ${e.message}", e)
            }
        }
    }



    override suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerProductoPorId(id)?.toProducto()
    }

    override suspend fun insertarProductos(productos: List<Producto>) {
        val entities = productos.map { it.toEntity() }
        productoDao.insertarProductos(entities)
    }

    override suspend fun insertarProducto(producto: Producto): Long {
        return productoDao.insertarProducto(producto.toEntity())
    }

    override suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizarProducto(producto.toEntity())
    }

    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto.toEntity())
    }

    override suspend fun eliminarTodosLosProductos() {
        productoDao.eliminarTodosLosProductos()
    }
}