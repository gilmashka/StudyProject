package com.example.studyproject_5.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyproject_5.presentation.screens.feed.AddPostScreen
import com.example.studyproject_5.presentation.screens.auth.LoginScreen
import com.example.studyproject_5.presentation.screens.auth.RegisterScreen
import com.example.studyproject_5.presentation.screens.feed.FeedScreen
import com.example.studyproject_5.presentation.screens.profile.ProfileScreen

@Composable
fun TravelNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Feed.route) {
            FeedScreen(navController)
        }
        composable(Screen.AddPost.route) {
            AddPostScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}