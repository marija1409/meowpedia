package com.example.meowpedia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.meowpedia.cats.details.breedDetails
import com.example.meowpedia.cats.list.cats

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost( //
        navController = navController,
        startDestination = "breeds"
    ) {
        cats(
            route = "breeds",
            onBreedClick = {
                navController.navigate(route = "breeds/$it")
            }
        )
        breedDetails(
            route = "breeds/{Id}",
            onClose = {
                navController.navigateUp()
            }
        )
    }
}