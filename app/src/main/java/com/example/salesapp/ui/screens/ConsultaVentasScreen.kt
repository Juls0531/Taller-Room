package com.example.salesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.salesapp.data.entities.VentaConArticulo
import com.example.salesapp.ui.components.FormSectionCard
import com.example.salesapp.ui.components.SalesGradientBackground
import com.example.salesapp.ui.components.SectionHeader
import com.example.salesapp.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaVentasScreen(
    viewModel: SalesViewModel,
    onNavigateBack: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var grupoSeleccionadoLocal by remember { mutableStateOf<Int?>(null) }
    var ventaParaDialog by remember { mutableStateOf<VentaConArticulo?>(null) }

    val grupos by viewModel.grupos.collectAsStateWithLifecycle()
    val ventasDelGrupo by viewModel.ventasDelGrupo.collectAsStateWithLifecycle()
    val grupoConsultado by viewModel.grupoSeleccionado.collectAsStateWithLifecycle()
    val totalGrupo = ventasDelGrupo.sumOf { it.valorTotal }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Consulta de ventas",
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        SalesGradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                SectionHeader(
                    eyebrow = "Reportes",
                    title = "Ventas por grupo",
                    subtitle = "Consulta los registros guardados y revisa el total vendido por cada grupo."
                )

                FormSectionCard(
                    title = "Buscar ventas",
                    description = "Selecciona un grupo para consultar sus ventas registradas."
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = grupoSeleccionadoLocal?.toString() ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Grupo") },
                                placeholder = {
                                    Text(
                                        if (grupos.isEmpty()) "No hay grupos"
                                        else "Seleccionar"
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Expandir"
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )

                            ExposedDropdownMenu(
                                expanded = dropdownExpanded && grupos.isNotEmpty(),
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                grupos.forEach { grupo ->
                                    DropdownMenuItem(
                                        text = { Text("Grupo $grupo") },
                                        onClick = {
                                            grupoSeleccionadoLocal = grupo
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                grupoSeleccionadoLocal?.let {
                                    viewModel.buscarVentasPorGrupo(it)
                                }
                            },
                            enabled = grupoSeleccionadoLocal != null,
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Buscar",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                when {
                    grupoConsultado == null -> {
                        EmptyStateMessage("Selecciona un grupo y presiona Buscar")
                    }

                    ventasDelGrupo.isEmpty() -> {
                        EmptyStateMessage("No hay ventas registradas para el Grupo $grupoConsultado")
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Grupo $grupoConsultado",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Text(
                                    text = "Resultado de la consulta",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "${ventasDelGrupo.size} ventas",
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    labelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = ventasDelGrupo,
                                key = { it.ventaId }
                            ) { venta ->
                                VentaItem(
                                    venta = venta,
                                    onClick = { ventaParaDialog = venta }
                                )
                            }
                        }

                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TOTAL DEL GRUPO",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "Suma de todas las ventas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f)
                                    )
                                }

                                Text(
                                    text = "$ $totalGrupo",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    ventaParaDialog?.let { venta ->
        VentaDetailDialog(
            venta = venta,
            onDismiss = { ventaParaDialog = null }
        )
    }
}

@Composable
private fun VentaItem(
    venta: VentaConArticulo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = venta.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = venta.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabelValue(
                    label = "Precio",
                    value = "$ ${venta.precioUnitario}"
                )

                LabelValue(
                    label = "Cantidad",
                    value = "${venta.cantidad}"
                )

                LabelValue(
                    label = "Valor",
                    value = "$ ${venta.valorTotal}",
                    highlight = true
                )
            }
        }
    }
}

@Composable
private fun LabelValue(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (highlight) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
private fun VentaDetailDialog(
    venta: VentaConArticulo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.large,
        title = {
            Text(
                text = venta.nombre,
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DialogRow("Descripción", venta.descripcion)

                HorizontalDivider()

                DialogRow("Precio unitario", "$ ${venta.precioUnitario}")
                DialogRow("Cantidad", "${venta.cantidad}")

                HorizontalDivider()

                DialogRow(
                    label = "Valor total",
                    value = "$ ${venta.valorTotal}",
                    highlight = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun DialogRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (highlight) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 34.dp, horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}