package com.example.salesapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articulos")
data class Articulo(
    @PrimaryKey
    val codigo: Int,
    val nombre: String,
    val descripcion: String,
    val precioUnitario: Int
)
