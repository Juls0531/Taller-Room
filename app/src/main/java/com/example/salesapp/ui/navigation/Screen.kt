package com.example.salesapp.ui.navigation


sealed class Screen(val route: String) {
    object Home : Screen("home")

    object RegistroArticulo : Screen("registro_articulo")

    object RegistroVenta : Screen("registro_venta")

    object ConsultaVentas : Screen("consulta_ventas")
}
