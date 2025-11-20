package com.example.huertohogarmobiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Usar el ViewModel de Compose
import androidx.navigation.compose.rememberNavController
import com.example.huertohogarmobiles.data.local.AppDatabase // <-- USAR TU CLASE EXISTENTE
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.ui.navigation.NavGraph
import com.example.huertohogarmobiles.ui.theme.HuertoHogarMobilesTheme
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModel
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Base de Datos y DAOs
        // Usamos AppDatabase, que es la clase que existe en tu proyecto
        val database = AppDatabase.getDatabase(applicationContext)
        val productoDao = database.productoDao()
        val carritoDao = database.carritoDao()

        // 2. Repositorios
        val productoRepository = ProductoRepositoryImpl(productoDao)
        val carritoRepository = CarritoRepository(carritoDao)

        // 3. Preferencias de Usuario
        val preferenciasManager = PreferenciasManager(applicationContext)

        setContent {
            HuertoHogarMobilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // 4. ViewModel (Inyección con Compose ViewModel helper)
                    val productoViewModel: ProductoViewModel = viewModel(
                        factory = ProductoViewModelFactory(productoRepository)
                    )

                    NavGraph(
                        navController = navController,
                        productoRepository = productoRepository,
                        carritoRepository = carritoRepository,
                        preferenciasManager = preferenciasManager,
                        productoViewModel = productoViewModel
                    )
                }
            }
        }
    }
}