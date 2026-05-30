package com.itis.artistinfodagger.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object ArtistDetails : Screen("artist_details/{artistId}") {
        fun passId(id: Int): String = "artist_details/$id"
    }
    data object PieChartInput : Screen("piechart_input")
    data object PieChartDisplay : Screen("piechart_display")
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
        composable(Screen.Search.route) {
            Text("Search Screen - временно")
        }

        composable(
            route = Screen.ArtistDetails.route,
            arguments = listOf(navArgument("artistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getInt("artistId") ?: return@composable
            Text("Details Screen for artist $artistId - временно")
        }

        composable(Screen.PieChartInput.route) {
            Text("PieChart Input Screen - временно")
        }

        composable(Screen.PieChartDisplay.route) {
            Text("PieChart Display Screen - временно")
        }
    }
}