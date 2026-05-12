package com.example.mealapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Saffron,
    onPrimary = Charcoal,
    primaryContainer = SaffronLight,
    secondary = Paprika,
    onSecondary = Cream,
    background = Cream,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    tertiary = GreenHerb,
)

@Composable
fun MealAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
