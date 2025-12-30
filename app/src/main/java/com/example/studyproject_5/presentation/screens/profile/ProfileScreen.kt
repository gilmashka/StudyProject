package com.example.studyproject_5.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyproject_5.R
import com.example.studyproject_5.presentation.navigation.Screen
import com.example.studyproject_5.presentation.viewmodel.ProfileViewModel
import com.example.studyproject_5.presentation.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(context)
    )

    val username by profileViewModel.username.collectAsState()

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            errorMessage?.let {
                Text(
                    text = it,
                    color = androidx.compose.ui.graphics.Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (username.isEmpty()) {
                CircularProgressIndicator()
            } else {
                Text(text = "Пользователь: $username")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                profileViewModel.logout { success ->
                    if (success) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        errorMessage = "Ошибка выхода"
                    }
                }
            }) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}