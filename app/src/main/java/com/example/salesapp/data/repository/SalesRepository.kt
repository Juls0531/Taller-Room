package com.example.salesapp.data.repository

import com.example.salesapp.data.dao.ArticuloDao
import com.example.salesapp.data.dao.VentaDao
import com.example.salesapp.data.entities.Articulo
import com.example.salesapp.data.entities.Venta
import com.example.salesapp.data.entities.VentaConArticulo
import kotlinx.coroutines.flow.Flow


class SalesRepository(
    private val articuloDao: ArticuloDao,
    private val ventaDao: VentaDao
) {

    // ─── Artículos ────────────────────────────────────────────────────────────
    val todosLosArticulos: Flow<List<Articulo>> = articuloDao.obtenerTodosLosArticulos()


    suspend fun insertarArticulo(articulo: Articulo) {
        articuloDao.insertarArticulo(articulo)
    }

    suspend fun existeArticulo(codigo: Int): Boolean {
        return articuloDao.existeArticulo(codigo) > 0
    }

    // ─── Ventas ───────────────────────────────────────────────────────────────

    val gruposConVentas: Flow<List<Int>> = ventaDao.obtenerGrupos()

    suspend fun insertarVenta(venta: Venta) {
        ventaDao.insertarVenta(venta)
    }

    fun obtenerVentasPorGrupo(grupo: Int): Flow<List<VentaConArticulo>> {
        return ventaDao.obtenerVentasPorGrupo(grupo)
    }
}
