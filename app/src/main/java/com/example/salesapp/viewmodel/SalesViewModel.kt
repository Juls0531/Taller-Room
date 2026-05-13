package com.example.salesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.entities.Articulo
import com.example.salesapp.data.entities.Venta
import com.example.salesapp.data.entities.VentaConArticulo
import com.example.salesapp.data.repository.SalesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ─── Estados de UI ────────────────────────────────────────────────────────────

sealed class SaveResult {
    object Idle : SaveResult()
    object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}


class SalesViewModel(private val repository: SalesRepository) : ViewModel() {

    // ─── Artículos ────────────────────────────────────────────────────────────


    val articulos: StateFlow<List<Articulo>> = repository.todosLosArticulos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val _saveArticuloResult = MutableStateFlow<SaveResult>(SaveResult.Idle)
    val saveArticuloResult: StateFlow<SaveResult> = _saveArticuloResult.asStateFlow()


    fun guardarArticulo(codigo: String, nombre: String, descripcion: String, precio: String) {
        if (codigo.isBlank() || nombre.isBlank() || descripcion.isBlank() || precio.isBlank()) {
            _saveArticuloResult.value = SaveResult.Error("Todos los campos son obligatorios")
            return
        }

        val codigoInt = codigo.toIntOrNull()
        val precioInt = precio.toIntOrNull()

        if (codigoInt == null) {
            _saveArticuloResult.value = SaveResult.Error("El código debe ser un número entero")
            return
        }
        if (precioInt == null || precioInt <= 0) {
            _saveArticuloResult.value = SaveResult.Error("El precio debe ser un número positivo")
            return
        }

        viewModelScope.launch {
            try {
                val articulo = Articulo(
                    codigo = codigoInt,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    precioUnitario = precioInt
                )
                repository.insertarArticulo(articulo)
                _saveArticuloResult.value = SaveResult.Success
            } catch (e: Exception) {
                _saveArticuloResult.value = SaveResult.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun resetSaveArticuloResult() {
        _saveArticuloResult.value = SaveResult.Idle
    }

    // ─── Ventas ───────────────────────────────────────────────────────────────

    val grupos: StateFlow<List<Int>> = repository.gruposConVentas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val _saveVentaResult = MutableStateFlow<SaveResult>(SaveResult.Idle)
    val saveVentaResult: StateFlow<SaveResult> = _saveVentaResult.asStateFlow()


    fun guardarVenta(articuloSeleccionado: Articulo?, grupo: String, cantidad: String) {
        if (articuloSeleccionado == null) {
            _saveVentaResult.value = SaveResult.Error("Seleccione un artículo")
            return
        }
        if (grupo.isBlank() || cantidad.isBlank()) {
            _saveVentaResult.value = SaveResult.Error("Todos los campos son obligatorios")
            return
        }

        val grupoInt = grupo.toIntOrNull()
        val cantidadInt = cantidad.toIntOrNull()

        if (grupoInt == null || grupoInt <= 0) {
            _saveVentaResult.value = SaveResult.Error("El grupo debe ser un número entero positivo")
            return
        }
        if (cantidadInt == null || cantidadInt <= 0) {
            _saveVentaResult.value = SaveResult.Error("La cantidad debe ser un número entero positivo")
            return
        }

        viewModelScope.launch {
            try {
                val venta = Venta(
                    articuloCodigo = articuloSeleccionado.codigo,
                    grupo = grupoInt,
                    cantidad = cantidadInt
                )
                repository.insertarVenta(venta)
                _saveVentaResult.value = SaveResult.Success
            } catch (e: Exception) {
                _saveVentaResult.value = SaveResult.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun resetSaveVentaResult() {
        _saveVentaResult.value = SaveResult.Idle
    }

    // ─── Consulta de ventas ───────────────────────────────────────────────────

    private val _grupoSeleccionado = MutableStateFlow<Int?>(null)
    val grupoSeleccionado: StateFlow<Int?> = _grupoSeleccionado.asStateFlow()

    private val _ventasDelGrupo = MutableStateFlow<List<VentaConArticulo>>(emptyList())
    val ventasDelGrupo: StateFlow<List<VentaConArticulo>> = _ventasDelGrupo.asStateFlow()

    private var consultaJob: kotlinx.coroutines.Job? = null


    fun buscarVentasPorGrupo(grupo: Int) {
        _grupoSeleccionado.value = grupo
        consultaJob?.cancel()
        consultaJob = viewModelScope.launch {
            repository.obtenerVentasPorGrupo(grupo).collect { ventas ->
                _ventasDelGrupo.value = ventas
            }
        }
    }

    // ─── Factory ──────────────────────────────────────────────────────────────


    class Factory(private val repository: SalesRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
                return SalesViewModel(repository) as T
            }
            throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
