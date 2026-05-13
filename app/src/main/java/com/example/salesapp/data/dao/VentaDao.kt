package com.example.salesapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salesapp.data.entities.Venta
import com.example.salesapp.data.entities.VentaConArticulo
import kotlinx.coroutines.flow.Flow


@Dao
interface VentaDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarVenta(venta: Venta)


    @Query("SELECT DISTINCT grupo FROM ventas ORDER BY grupo ASC")
    fun obtenerGrupos(): Flow<List<Int>>


    @Query("""
        SELECT 
            v.id AS ventaId,
            v.grupo,
            v.cantidad,
            a.nombre,
            a.descripcion,
            a.precioUnitario
        FROM ventas v
        INNER JOIN articulos a ON v.articuloCodigo = a.codigo
        WHERE v.grupo = :grupo
        ORDER BY a.nombre ASC
    """)
    fun obtenerVentasPorGrupo(grupo: Int): Flow<List<VentaConArticulo>>
}
