package com.example.salesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.salesapp.ui.components.HeroHeader
import com.example.salesapp.ui.components.MenuActionCard
import com.example.salesapp.ui.components.SalesGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToConsulta: () -> Unit,
    onNavigateToRegistroVenta: () -> Unit,
    onNavigateToRegistroArticulo: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SalesApp",
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        SalesGradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                HeroHeader(
                    title = "Sistema de Ventas",
                    subtitle = "Registra artículos, crea ventas y consulta el total de cada grupo de forma rápida."
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Menú principal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                MenuActionCard(
                    title = "Consulta de Ventas",
                    description = "Busca las ventas registradas por grupo y revisa sus totales.",
                    icon = Icons.Default.Search,
                    onClick = onNavigateToConsulta
                )

                MenuActionCard(
                    title = "Registro de Ventas",
                    description = "Selecciona un artículo, define cantidad y guarda una venta.",
                    icon = Icons.Default.Add,
                    onClick = onNavigateToRegistroVenta
                )

                MenuActionCard(
                    title = "Registro de Artículos",
                    description = "Agrega productos nuevos al catálogo de la aplicación.",
                    icon = Icons.Default.List,
                    onClick = onNavigateToRegistroArticulo
                )
            }
        }
    }
}