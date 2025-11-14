package com.example.huertohogarmobiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.huertohogarmobiles.data.local.HuertoHogarDB
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

        // --- INYECCIÓN DE DEPENDENCIAS MANUAL ---

        // 1. Base de Datos y DAOs
        val database = HuertoHogarDB.getDatabase(this)
        val productoDao = database.productoDao()
        val carritoDao = database.carritoDao()

        // 2. Repositorios
        val productoRepository = ProductoRepositoryImpl(productoDao)
        val carritoRepository = CarritoRepository(carritoDao)

        // 3. Preferencias de Usuario
        val preferenciasManager = PreferenciasManager(this)

        // 4. ViewModel (con su Factory)
        val productoViewModel: ProductoViewModel by viewModels {
            ProductoViewModelFactory(productoRepository)
        }

        setContent {
            HuertoHogarMobilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
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