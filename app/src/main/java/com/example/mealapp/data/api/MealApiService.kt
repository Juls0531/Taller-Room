package com.example.mealapp.data.api

import com.example.mealapp.data.model.MealResponse
import retrofit2.http.GET

interface MealApiService {
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
}
