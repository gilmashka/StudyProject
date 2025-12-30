package com.example.studyproject_5.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.studyproject_5.presentation.viewmodel.AuthViewModel
import com.example.studyproject_5.presentation.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(context)
    )

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            errorMessage?.let {
                Text(
                    text = it,
                    color = androidx.compose.ui.graphics.Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                    if (password != confirmPassword) {
                        errorMessage = context.getString(R.string.error_passwords_not_match)
                        return@Button
                    }
                    isLoading = true
                    authViewModel.register(username, password) { success, messageResId ->
                        isLoading = false
                        if (success) {
                            navController.popBackStack()
                        } else {
                            errorMessage = if (messageResId != null) {
                                context.getString(messageResId)
                            } else {
                                context.getString(R.string.unexpected_error)
                            }
                        }
                    }
                } else {
                    errorMessage = context.getString(R.string.error_field_required)
                }
            }) {
                Text(stringResource(R.string.register))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}