package com.example.salesapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SalesPrimary = Color(0xFF2563EB)
private val SalesSecondary = Color(0xFF14B8A6)
private val SalesAccent = Color(0xFFF97316)
private val SalesBackground = Color(0xFFF4F7FB)
private val SalesSurface = Color(0xFFFFFFFF)
private val SalesText = Color(0xFF111827)

private val LightColorScheme = lightColorScheme(
    primary = SalesPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEBFF),
    onPrimaryContainer = Color(0xFF0F172A),

    secondary = SalesSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD9FBF6),
    onSecondaryContainer = Color(0xFF0F172A),

    tertiary = SalesAccent,
    onTertiary = Color.White,

    background = SalesBackground,
    onBackground = SalesText,

    surface = SalesSurface,
    onSurface = SalesText,

    surfaceVariant = Color(0xFFE9EEF6),
    onSurfaceVariant = Color(0xFF64748B),

    error = Color(0xFFDC2626),
    onError = Color.White
)

private val SalesShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(34.dp)
)

@Composable
fun SalesAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes = SalesShapes,
        content = content
    )
}