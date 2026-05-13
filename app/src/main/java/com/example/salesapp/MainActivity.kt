package com.example.salesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.salesapp.data.database.SalesDatabase
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.ui.navigation.SalesNavGraph
import com.example.salesapp.ui.theme.SalesAppTheme
import com.example.salesapp.viewmodel.SalesViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SalesDatabase.getDatabase(applicationContext)
        val repository = SalesRepository(
            articuloDao = database.articuloDao(),
            ventaDao = database.ventaDao(),
        )
        val viewModelFactory = SalesViewModel.Factory(repository)

        setContent {
            SalesAppTheme {
                val navController = rememberNavController()

                val viewModel: SalesViewModel = viewModel(factory = viewModelFactory)

                SalesNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
