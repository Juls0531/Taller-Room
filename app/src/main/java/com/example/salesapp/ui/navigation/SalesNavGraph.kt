package com.example.salesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.salesapp.ui.screens.ConsultaVentasScreen
import com.example.salesapp.ui.screens.HomeScreen
import com.example.salesapp.ui.screens.RegistroArticuloScreen
import com.example.salesapp.ui.screens.RegistroVentaScreen
import com.example.salesapp.viewmodel.SalesViewModel


@Composable
fun SalesNavGraph(
    navController: NavHostController,
    viewModel: SalesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // ── Pantalla principal ────────────────────────────────────────────────
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToConsulta = { navController.navigate(Screen.ConsultaVentas.route) },
                onNavigateToRegistroVenta = { navController.navigate(Screen.RegistroVenta.route) },
                onNavigateToRegistroArticulo = { navController.navigate(Screen.RegistroArticulo.route) }
            )
        }

        // ── Registro de artículos ─────────────────────────────────────────────
        composable(route = Screen.RegistroArticulo.route) {
            RegistroArticuloScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Registro de ventas ────────────────────────────────────────────────
        composable(route = Screen.RegistroVenta.route) {
            RegistroVentaScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Consulta de ventas ────────────────────────────────────────────────
        composable(route = Screen.ConsultaVentas.route) {
            ConsultaVentasScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
