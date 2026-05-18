package com.artistinfo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.artistinfo.network.ServiceLocator
import com.artistinfo.presentation.navigation.ArtistInfoNavigation
import com.artistinfo.presentation.screens.SearchScreen
import com.artistinfo.presentation.ui.theme.ArtistInfoTheme
import com.artistinfo.presentation.viewModels.SearchArtistViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = SearchArtistViewModel(
            searchArtistUseCase = ServiceLocator.getSearchArtistUseCase()
        )

        enableEdgeToEdge()
        setContent {
            ArtistInfoTheme {
                ArtistInfoNavigation(
                    viewModel = viewModel
                )
            }
        }
    }
}

