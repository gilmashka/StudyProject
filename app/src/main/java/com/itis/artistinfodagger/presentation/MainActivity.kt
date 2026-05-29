package com.itis.artistinfodagger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itis.artistinfodagger.presentation.screen.DetailsScreen
import com.itis.artistinfodagger.presentation.screen.SearchScreen
import com.itis.artistinfodagger.presentation.viewmodel.DetailsViewModel
import com.itis.artistinfodagger.presentation.viewmodel.DetailsViewModelFactory
import okhttp3.OkHttpClient
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as ArtistInfoDaggerApplication).appComponent
        appComponent.inject(this)


        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(OkHttpNetworkFetcherFactory(OkHttpClient.Builder().build()))
            }
            .build()

        setContent {
            AppNavigation(detailsViewModelFactory, imageLoader)
        }
    }
}

@Composable
fun AppNavigation(
    detailsViewModelFactory: DetailsViewModelFactory,
    imageLoader: ImageLoader
) {
    val navController = rememberNavController()

    val onArtistClick = remember(navController) {
        { artistId: Int ->
            navController.navigate("details/$artistId")
        }
    }

    val appComponent = (LocalContext.current.applicationContext as ArtistInfoDaggerApplication).appComponent
    val searchViewModel =  remember { appComponent.getSearchViewModel() }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val screen = entry.destination.route ?: "unknown"
            FirebaseCrashlytics.getInstance().log("Screen: $screen")
            FirebaseCrashlytics.getInstance().setCustomKey("last_screen", screen)
        }
    }

    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            SearchScreen(
                viewModel = searchViewModel,
                onArtistClick = onArtistClick,
                imageLoader = imageLoader
            )
        }
        composable(
            route = "details/{artistId}",
            arguments = listOf(navArgument("artistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getInt("artistId") ?: return@composable
            val detailsViewModel = remember(artistId) {
                detailsViewModelFactory.create(artistId)
            }

            DetailsScreen(
                viewModel = detailsViewModel,
                imageLoader = imageLoader
            )
        }
    }
}