package com.example.studyproject_5.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Feed : Screen("feed")
    object AddPost : Screen("add_post")
    object Profile : Screen("profile")
}