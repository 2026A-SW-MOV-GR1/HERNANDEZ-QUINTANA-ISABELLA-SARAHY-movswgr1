package com.epn.mockcrud.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epn.mockcrud.viewmodel.ItemViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    // Obtenemos el ViewModel aquí para que sea compartido por todo el NavHost
    val mainViewModel: ItemViewModel = viewModel()

    NavHost(navController, startDestination = "list") {
        composable("list") {
            ListScreen(
                onNavigateToCreate = { navController.navigate("form/new") },
                onNavigateToEdit   = { id -> navController.navigate("form/$id") },
                onNavigateToPosts  = { navController.navigate("posts") },
                onNavigateToSecrets = { navController.navigate("secrets") },
                viewModel = mainViewModel
            )
        }
        composable(
            "form/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStack ->
            val raw = backStack.arguments?.getString("itemId")
            val bookId = raw?.toLongOrNull()
            FormScreen(
                bookId = bookId, 
                onNavigateBack = { navController.popBackStack() },
                viewModel = mainViewModel
            )
        }
        composable("posts") {
            PostScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("secrets") {
            SecretsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
