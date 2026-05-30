package com.itis.artistinfodagger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itis.artistinfodagger.piechart.presentation.screen.PieChartDisplayScreen
import com.itis.artistinfodagger.piechart.presentation.screen.PieChartInputScreen
import com.itis.artistinfodagger.piechart.presentation.viewmodel.PieChartViewModel
import com.itis.artistinfodagger.presentation.screen.DetailsScreen
import com.itis.artistinfodagger.presentation.screen.SearchScreen
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val pieChartViewModel = remember { PieChartViewModel() }

    val onArtistClick = remember(navController) {
        { artistId: Int ->
            navController.navigate("details/$artistId")
        }
    }

    val appComponent = (LocalContext.current.applicationContext as ArtistInfoDaggerApplication).appComponent
    val searchViewModel = remember { appComponent.getSearchViewModel() }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val screen = entry.destination.route ?: "unknown"
            FirebaseCrashlytics.getInstance().log("Screen: $screen")
            FirebaseCrashlytics.getInstance().setCustomKey("last_screen", screen)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavigationItem.Search,
                    NavigationItem.PieChart
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("search") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.padding(paddingValues)
        ) {
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

            composable("piechart_input") {
                PieChartInputScreen(
                    viewModel = pieChartViewModel,
                    onNavigateToDisplay = {
                        navController.navigate("piechart_display")
                    }
                )
            }

            composable("piechart_display") {
                PieChartDisplayScreen(
                    viewModel = pieChartViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Search : NavigationItem(
        route = "search",
        title = "Поиск",
        icon = Icons.Default.Search
    )

    data object PieChart : NavigationItem(
        route = "piechart_input",
        title = "Диаграмма",
        icon = Icons.Default.AddCircle
    )
}