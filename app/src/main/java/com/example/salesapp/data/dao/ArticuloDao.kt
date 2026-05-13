package com.example.salesapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salesapp.data.entities.Articulo
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticuloDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarArticulo(articulo: Articulo)


    @Query("SELECT * FROM articulos ORDER BY nombre ASC")
    fun obtenerTodosLosArticulos(): Flow<List<Articulo>>


    @Query("SELECT COUNT(*) FROM articulos WHERE codigo = :codigo")
    suspend fun existeArticulo(codigo: Int): Int
}
