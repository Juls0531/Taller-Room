package com.example.salesapp.data.entities


data class VentaConArticulo(
    val ventaId: Int,
    val grupo: Int,
    val cantidad: Int,
    val nombre: String,
    val descripcion: String,
    val precioUnitario: Int
) {
    val valorTotal: Int get() = precioUnitario * cantidad
}
