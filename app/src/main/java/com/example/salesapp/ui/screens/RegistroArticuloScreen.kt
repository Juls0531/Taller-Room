package com.example.salesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.salesapp.ui.components.FormSectionCard
import com.example.salesapp.ui.components.SalesGradientBackground
import com.example.salesapp.ui.components.SectionHeader
import com.example.salesapp.viewmodel.SaveResult
import com.example.salesapp.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroArticuloScreen(
    viewModel: SalesViewModel,
    onNavigateBack: () -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    val saveResult by viewModel.saveArticuloResult.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saveResult) {
        when (val result = saveResult) {
            is SaveResult.Success -> {
                snackbarHostState.showSnackbar("Artículo guardado exitosamente ✓")
                codigo = ""
                nombre = ""
                descripcion = ""
                precio = ""
                viewModel.resetSaveArticuloResult()
            }

            is SaveResult.Error -> {
                snackbarHostState.showSnackbar("Error: ${result.message}")
                viewModel.resetSaveArticuloResult()
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
                        text = "Nuevo artículo",
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
                    eyebrow = "Catálogo",
                    title = "Registra un artículo",
                    subtitle = "Crea productos claros para que las ventas sean más rápidas y ordenadas."
                )

                FormSectionCard(
                    title = "Datos del artículo",
                    description = "Completa la información básica del producto que deseas registrar."
                ) {
                    OutlinedTextField(
                        value = codigo,
                        onValueChange = { if (it.all(Char::isDigit)) codigo = it },
                        label = { Text("Código *") },
                        placeholder = { Text("Ej: 1001") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre *") },
                        placeholder = { Text("Ej: Laptop Dell") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción *") },
                        placeholder = { Text("Ej: Laptop 15 pulgadas, 8GB RAM") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { if (it.all(Char::isDigit)) precio = it },
                        label = { Text("Precio unitario *") },
                        placeholder = { Text("Ej: 2500000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        prefix = { Text("$ ") }
                    )

                    Button(
                        onClick = {
                            viewModel.guardarArticulo(
                                codigo = codigo,
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio
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
                            text = "Guardar artículo",
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