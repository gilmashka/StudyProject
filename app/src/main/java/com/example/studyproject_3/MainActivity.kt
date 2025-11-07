package com.example.studyproject_3

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studyproject_3.ui.screens.EditNotificationScreen
import com.example.studyproject_3.ui.screens.NotificationSettingsScreen
import com.example.studyproject_3.ui.screens.UserMessagesScreen
import com.example.studyproject_3.ui.theme.StudyProject_3Theme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (hasPermission != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val title = intent.getStringExtra("notification_title")
        val message = intent.getStringExtra("notification_message")
        if (title != null && message != null) {
            Toast.makeText(this, getString(R.string.from_notification, title), Toast.LENGTH_SHORT).show()
        }

        setContent {
            StudyProject_3Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp()
                }
            }
        }
    }
}
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { },
                    label = { Text("Settings") },
                    selected = currentBackStackEntry.value?.destination?.route == "notificationSettings",
                    onClick = {
                        navController.navigate("notificationSettings") {
                            popUpTo("notificationSettings") { saveState = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { },
                    label = { Text("Edit") },
                    selected = currentBackStackEntry.value?.destination?.route == "editNotification",
                    onClick = {
                        navController.navigate("editNotification") {
                            popUpTo("editNotification") { saveState = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { },
                    label = { Text("Messages") },
                    selected = currentBackStackEntry.value?.destination?.route == "userMessages",
                    onClick = {
                        navController.navigate("userMessages") {
                            popUpTo("userMessages") { saveState = true }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "notificationSettings",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("notificationSettings") {
                NotificationSettingsScreen()
            }
            composable("editNotification") {
                EditNotificationScreen()
            }
            composable("userMessages") {
                UserMessagesScreen()
            }
        }
    }
}