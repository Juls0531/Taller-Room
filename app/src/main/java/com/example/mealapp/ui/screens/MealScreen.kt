package com.example.mealapp.ui.screens

import android.content.Intent
import androidx.compose.animation.core.*
import androidx.core.net.toUri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mealapp.MealUiState
import com.example.mealapp.MealViewModel
import com.example.mealapp.data.model.Meal
import com.example.mealapp.ui.theme.*

@Composable
fun MealScreen(viewModel: MealViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    MealAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream),
        ) {
            when (val state = uiState) {
                is MealUiState.Loading -> LoadingScreen()
                is MealUiState.Success -> MealContent(
                    meal = state.meal,
                ) { viewModel.fetchRandomMeal() }
                is MealUiState.Error -> ErrorScreen(
                    message = state.message,
                ) { viewModel.fetchRandomMeal() }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
        ),
        label = "rotation",
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .rotate(rotation)
                    .border(4.dp, Saffron, CircleShape)
                    .clip(CircleShape),
            )
            Text(
                text = "Buscando receta...",
                fontSize = 18.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Text(text = "🍽️", fontSize = 64.sp)
            Text(
                text = "¡Oops!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Paprika,
            )
            Text(
                text = message,
                fontSize = 15.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Saffron),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("Reintentar", color = Charcoal, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun MealContent(meal: Meal, onRefresh: () -> Unit) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            // Hero Image with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.strMealThumb)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                // Bottom gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Cream.copy(alpha = 0.6f),
                                    Cream
                                )
                            )
                        )
                )
                // Tags at top
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    meal.strCategory?.let { category ->
                        MealTag(text = category, backgroundColor = Saffron.copy(alpha = 0.9f))
                    }
                    meal.strArea?.let { area ->
                        MealTag(text = area, backgroundColor = Paprika.copy(alpha = 0.85f), textColor = Cream)
                    }
                }
            }

            // Content card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp)
            ) {
                // Title
                Text(
                    text = meal.strMeal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(bottom = 24.dp),
                )

                // Ingredients section
                SectionTitle(title = "🧂 Ingredientes")
                Spacer(modifier = Modifier.height(12.dp))

                val ingredients = meal.getIngredients()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ingredients.forEachIndexed { index, (measure, ingredient) ->
                            IngredientRow(measure = measure, ingredient = ingredient)
                            if (index < (ingredients.size - 1)) {
                                HorizontalDivider(
                                    color = DividerColor,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Instructions section
                SectionTitle(title = "👨‍🍳 Preparación")
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Text(
                        text = meal.strInstructions ?: "Sin instrucciones disponibles.",
                        fontSize = 15.sp,
                        color = TextPrimary,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // YouTube button (optional)
                if (!meal.strYoutube.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, meal.strYoutube.toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Paprika
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "▶  Ver en YouTube",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Bottom spacing for FAB
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onRefresh,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            containerColor = Saffron,
            contentColor = Charcoal,
            shape = RoundedCornerShape(18.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Nueva receta",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Nueva receta",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    )
}

@Composable
fun MealTag(
    text: String,
    backgroundColor: Color,
    textColor: Color = Charcoal
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun IngredientRow(measure: String, ingredient: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bullet dot
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Saffron)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = ingredient,
            fontSize = 15.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (measure.isNotBlank()) {
            Text(
                text = measure,
                fontSize = 14.sp,
                color = TextSecondary,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
