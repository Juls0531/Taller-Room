package com.example.salesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.salesapp.data.entities.Articulo
import com.example.salesapp.ui.components.FormSectionCard
import com.example.salesapp.ui.components.SalesGradientBackground
import com.example.salesapp.ui.components.SectionHeader
import com.example.salesapp.viewmodel.SaveResult
import com.example.salesapp.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVentaScreen(
    viewModel: SalesViewModel,
    onNavigateBack: () -> Unit
) {
    var articuloSeleccionado by remember { mutableStateOf<Articulo?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var grupo by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val articulos by viewModel.articulos.collectAsStateWithLifecycle()
    val saveResult by viewModel.saveVentaResult.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saveResult) {
        when (val result = saveResult) {
            is SaveResult.Success -> {
                snackbarHostState.showSnackbar("Venta registrada exitosamente ✓")
                articuloSeleccionado = null
                grupo = ""
                cantidad = ""
                viewModel.resetSaveVentaResult()
            }

            is SaveResult.Error -> {
                snackbarHostState.showSnackbar("Error: ${result.message}")
                viewModel.resetSaveVentaResult()
            }

            SaveResult.Idle -> Unit
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nueva venta",
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        SalesGradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                SectionHeader(
                    eyebrow = "Ventas",
                    title = "Registra una venta",
                    subtitle = "Selecciona un producto, agrega grupo y cantidad; la app calcula el valor estimado."
                )

                FormSectionCard(
                    title = "Datos de la venta",
                    description = "Completa la información necesaria para registrar la venta."
                ) {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = articuloSeleccionado?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Artículo *") },
                            placeholder = {
                                Text(
                                    if (articulos.isEmpty()) "No hay artículos registrados"
                                    else "Seleccione un artículo"
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
                            expanded = dropdownExpanded && articulos.isNotEmpty(),
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            articulos.forEach { articulo ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = articulo.nombre,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text(
                                                text = "Código: ${articulo.codigo} · $ ${articulo.precioUnitario}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        articuloSeleccionado = articulo
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    articuloSeleccionado?.let { art ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "Artículo seleccionado",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = art.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Text(
                                    text = art.descripcion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
                                )

                                Text(
                                    text = "Precio unitario: $ ${art.precioUnitario}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = grupo,
                            onValueChange = { if (it.all(Char::isDigit)) grupo = it },
                            label = { Text("Grupo *") },
                            placeholder = { Text("Ej: 1") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = cantidad,
                            onValueChange = { if (it.all(Char::isDigit)) cantidad = it },
                            label = { Text("Cantidad *") },
                            placeholder = { Text("Ej: 5") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    val cantidadInt = cantidad.toIntOrNull()

                    if (
                        articuloSeleccionado != null &&
                        cantidadInt != null &&
                        cantidadInt > 0
                    ) {
                        val valorEstimado = articuloSeleccionado!!.precioUnitario * cantidadInt

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Valor estimado",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )

                                    Text(
                                        text = "Precio por cantidad",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.72f)
                                    )
                                }

                                Text(
                                    text = "$ $valorEstimado",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.guardarVenta(
                                articuloSeleccionado = articuloSeleccionado,
                                grupo = grupo,
                                cantidad = cantidad
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = "Guardar venta",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Text(
                    text = "* Campos obligatorios",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}