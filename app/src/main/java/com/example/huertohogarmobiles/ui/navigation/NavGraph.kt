package com.example.huertohogarmobiles.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import com.example.huertohogarmobiles.data.repository.CarritoRepository
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.ui.screen.AdminPanelScreen
import com.example.huertohogarmobiles.ui.screen.CarritoScreen
import com.example.huertohogarmobiles.ui.screen.DetalleProductoScreen
import com.example.huertohogarmobiles.ui.screen.FormularioProductoScreen
import com.example.huertohogarmobiles.ui.screen.HomeScreen
import com.example.huertohogarmobiles.ui.screen.LoginAdminScreen
import com.example.huertohogarmobiles.ui.screen.PortadaScreen
import com.example.huertohogarmobiles.ui.screen.RegistroScreen
import com.example.huertohogarmobiles.ui.viewmodel.ProductoViewModel

/**
 * NavGraph: Define el contenedor de navegación y las transiciones entre pantallas.
 * Es una función @Composable.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    preferenciasManager: PreferenciasManager,
    productoViewModel: ProductoViewModel, // ViewModel compartido
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        // Decide la ruta inicial: si el admin está logueado, va al panel; si no, a la portada.
        startDestination = if (preferenciasManager.estaAdminLogueado()) Rutas.PANEL_ADMIN else Rutas.PORTADA,
        modifier = modifier
    ) {
        // --- RUTAS DE CLIENTE ---

        // Portada
        composable(route = Rutas.PORTADA) {
            PortadaScreen(
                onEntrarClick = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.PORTADA) { inclusive = true } // Home será la nueva raíz
                    }
                },
                onAdminClick = {
                    navController.navigate(Rutas.LOGIN_ADMIN)
                }
            )
        }

        // Home (Catálogo)
        composable(route = Rutas.HOME) {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                onProductoClick = { id ->
                    navController.navigate(Rutas.detalleConId(id))
                },
                onCarritoClick = {
                    navController.navigate(Rutas.CARRITO)
                },
                onRegistroClick = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onVolverPortada = {
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }

        // Detalle con parámetro (IMPLEMENTADO)
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument("productoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Carrito
        composable(route = Rutas.CARRITO) {
            CarritoScreen(
                carritoRepository = carritoRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Registro (IMPLEMENTADO)
        composable(route = Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = {
                    // Al ser exitoso, normalmente se vuelve a la pantalla anterior (Home o Portada)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- RUTAS DE ADMINISTRACIÓN ---

        // Login Admin
        composable(route = Rutas.LOGIN_ADMIN) {
            LoginAdminScreen(
                onLoginExitoso = {
                    // Navega al Panel Admin y limpia el back stack
                    navController.navigate(Rutas.PANEL_ADMIN) {
                        popUpTo(Rutas.LOGIN_ADMIN) { inclusive = true }
                    }
                },
                onVolverClick = { navController.popBackStack() },
                onValidarCredenciales = { user, pass ->
                    preferenciasManager.validarCredencialesAdmin(user, pass)
                },
                onGuardarSesion = { username ->
                    preferenciasManager.guardarSesionAdmin(username)
                }
            )
        }

        // Panel Admin
        composable(route = Rutas.PANEL_ADMIN) {
            val uiState by productoViewModel.uiState.collectAsState() // Observa el estado compartido

            AdminPanelScreen(
                productos = uiState.productos,
                usernameAdmin = preferenciasManager.obtenerUsernameAdmin() ?: "Admin",
                onAgregarProducto = {
                    // Navega al formulario con ID -1 para crear
                    navController.navigate(Rutas.formularioEditar(-1))
                },
                onEditarProducto = { producto ->
                    navController.navigate(Rutas.formularioEditar(producto.id))
                },
                onEliminarProducto = { producto ->
                    productoViewModel.eliminarProducto(producto)
                },
                onCerrarSesion = {
                    preferenciasManager.cerrarSesionAdmin()
                    // Vuelve a la portada y limpia el back stack
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.PANEL_ADMIN) { inclusive = true }
                    }
                }
            )
        }

        // Formulario Producto (Editar/Crear) (IMPLEMENTADO)
        composable(
            route = Rutas.FORMULARIO_PRODUCTO,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType; defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: -1
            FormularioProductoScreen(
                productoId = productoId,
                productoRepository = productoRepository,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
