package com.example.huertohogarmobiles.data.repository
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.toEntity
import com.example.huertohogarmobiles.data.local.entity.toProducto
import com.example.huertohogarmobiles.data.mapper.toEntity // <-- Asegúrate de tener este mapper DTO->Entity
import com.example.huertohogarmobiles.data.remote.api.ProductApiService // ✅ NUEVO: Importar la API Service
import com.example.huertohogarmobiles.domain.model.Producto
import com.example.huertohogarmobiles.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext // ✅ NUEVO: Para Coroutines
import kotlinx.coroutines.Dispatchers // ✅ NUEVO: Para Coroutines
import javax.inject.Inject // ✅ NUEVO: Para Inyección de Dependencias (Hilt)
import android.util.Log // ✅ NUEVO: Para manejo de errores

// Clase que implementa la estrategia Cache-First
// @Inject constructor es requerido si usas Hilt para inyectar esta clase
class ProductoRepositoryImpl @Inject constructor(
    private val productoDao: ProductoDao,
    private val apiService: ProductApiService, // ✅ NUEVO: Inyectar la API Service
    private val ioDispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO // Para ejecutar operaciones en hilo de IO
) : RepositorioProductos {

    // 1. Fuente Única de Verdad: La UI SIEMPRE lee desde Room.
    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductos()
            .map { entities ->
                entities.map { it.toProducto() }
            }
    }

    // 2. Función de Sincronización: Llamada por el ViewModel para obtener datos frescos.
    suspend fun refreshProducts() {
        withContext(ioDispatcher) {
            try {
                val response = apiService.getProducts()

                if (response.isSuccessful) {
                    val productsDto = response.body()
                    productsDto?.products?.let { dtos ->

                        // Mapear DTOs a Entities (Aquí debes tener la función toEntity)
                        val entities = dtos.map { it.toEntity() }

                        // Aplicar Estrategia Cache-First: Limpiar caché y guardar lo nuevo.
                        productoDao.eliminarTodosLosProductos() // ❗ Usamos tu nombre de DAO
                        productoDao.insertarProductos(entities) // ❗ Usamos tu nombre de DAO
                    }
                } else {
                    Log.e("ProductoRepo", "Error HTTP al refrescar: ${response.code()}")
                    // Aquí podrías lanzar una excepción si fuera crítico
                }
            } catch (e: Exception) {
                // Si la red falla, el error se atrapa aquí. El Flow de Room sigue emitiendo datos viejos.
                Log.e("ProductoRepo", "Error de Red/IO: ${e.message}", e)
            }
        }
    }

    // El resto de operaciones CRUD solo aplican a datos locales o a operaciones específicas (ej. carrito)

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