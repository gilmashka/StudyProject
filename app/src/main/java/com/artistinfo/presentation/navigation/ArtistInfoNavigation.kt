package com.artistinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artistinfo.domain.models.Artist
import com.artistinfo.presentation.screens.ExpandedInfoScreen
import com.artistinfo.presentation.screens.SearchScreen
import com.artistinfo.presentation.viewModels.SearchArtistViewModel

@Composable
fun ArtistInfoNavigation(
    viewModel: SearchArtistViewModel
) {
    val navController = rememberNavController()
    var selectedArtist by remember { mutableStateOf<Artist?>(null) }

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable("search") {
            SearchScreen(
                viewModel = viewModel,
                onArtistClick = { artist ->
                    selectedArtist = artist
                    navController.navigate("detail")
                }
            )
        }

        composable("detail") {
            selectedArtist?.let { artist ->
                ExpandedInfoScreen(
                    artist = artist,
                    navController = navController)
            }
        }
    }
}