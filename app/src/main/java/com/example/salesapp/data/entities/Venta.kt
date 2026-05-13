package com.example.salesapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(
            entity = Articulo::class,
            parentColumns = ["codigo"],
            childColumns = ["articuloCodigo"],
            onDelete = ForeignKey.CASCADE // Si se elimina el artículo, se eliminan sus ventas
        )
    ],
    indices = [Index(value = ["articuloCodigo"])]
)
data class Venta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val articuloCodigo: Int,
    val grupo: Int,
    val cantidad: Int
)
