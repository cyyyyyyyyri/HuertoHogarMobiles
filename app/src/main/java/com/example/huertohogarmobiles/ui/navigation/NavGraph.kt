package com.example.huertohogarmobiles.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.huertohogarmobiles.ui.screen.*

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.PORTADA,
        modifier = modifier
    ) {
        // Portada
        composable(route = Rutas.PORTADA) {
            PortadaScreen(
                onEntrarClick = { navController.navigate(Rutas.HOME) },
                onAdminClick = { navController.navigate(Rutas.LOGIN_ADMIN) }
            )
        }

        // Home
        composable(route = Rutas.HOME) {
            HomeScreen(
                onProductoClick = { id -> navController.navigate(Rutas.detalleConId(id)) },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onRegistroClick = { navController.navigate(Rutas.REGISTRO) },
                onVolverPortada = {
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Detalle
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Carrito
        composable(route = Rutas.CARRITO) {
            CarritoScreen(onBackClick = { navController.popBackStack() })
        }

        // Registro
        composable(route = Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Login Admin
        composable(route = Rutas.LOGIN_ADMIN) {
            LoginAdminScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.PANEL_ADMIN) {
                        popUpTo(Rutas.LOGIN_ADMIN) {
                            inclusive = true
                        }
                    }
                },
                onVolverClick = { navController.popBackStack() }
            )
        }

        // Panel Admin
        composable(route = Rutas.PANEL_ADMIN) {
            AdminPanelScreen(
                onAgregarProducto = { navController.navigate(Rutas.formularioEditar(-1)) },
                onEditarProducto = { producto ->
                    navController.navigate(
                        Rutas.formularioEditar(
                            producto.id
                        )
                    )
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.PORTADA) {
                        popUpTo(Rutas.PANEL_ADMIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Formulario
        composable(
            route = Rutas.FORMULARIO_PRODUCTO,
            arguments = listOf(navArgument("productoId") {
                type = NavType.IntType; defaultValue = -1
            })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: -1
            FormularioProductoScreen(
                productoId = productoId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}