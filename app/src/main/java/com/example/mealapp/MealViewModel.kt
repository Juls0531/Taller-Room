package com.example.mealapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.data.api.RetrofitClient
import com.example.mealapp.data.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MealUiState {
    object Loading : MealUiState()
    data class Success(val meal: Meal) : MealUiState()
    data class Error(val message: String) : MealUiState()
}

class MealViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MealUiState>(MealUiState.Loading)
    val uiState: StateFlow<MealUiState> = _uiState.asStateFlow()

    init {
        fetchRandomMeal()
    }

    fun fetchRandomMeal() {
        viewModelScope.launch {
            _uiState.value = MealUiState.Loading
            try {
                val response = RetrofitClient.mealApiService.getRandomMeal()
                val meal = response.meals?.firstOrNull()
                if (meal != null) {
                    _uiState.value = MealUiState.Success(meal)
                } else {
                    _uiState.value = MealUiState.Error("No se encontró la receta")
                }
            } catch (e: Exception) {
                _uiState.value = MealUiState.Error(
                    e.message ?: "Error de conexión. Verifica tu internet.",
                )
            }
        }
    }
}
