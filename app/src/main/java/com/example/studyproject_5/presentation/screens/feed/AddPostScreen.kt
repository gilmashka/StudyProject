package com.example.studyproject_5.presentation.screens.feed

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyproject_5.R
import com.example.studyproject_5.presentation.viewmodel.AddPostViewModel
import com.example.studyproject_5.presentation.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val addPostViewModel: AddPostViewModel = viewModel(
        factory = ViewModelFactory(context)
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bitmap = addPostViewModel.getBitmapFromUri(uri)
            bitmap?.let {
                selectedImageUri = it.asImageBitmap()
            }
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.add_post)) }
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

                selectedImageUri?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(onClick = {
                    imagePicker.launch("image/*")
                }) {
                    Text(stringResource(R.string.choose_photo))
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.post_title)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text(stringResource(R.string.post_country)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text(stringResource(R.string.latitude)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text(stringResource(R.string.longitude)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    if (title.isNotBlank() && country.isNotBlank() &&
                        latitude.isNotBlank() && longitude.isNotBlank() &&
                        selectedImageUri != null) {

                        isLoading = true
                        addPostViewModel.addPost(
                            title = title,
                            country = country,
                            latitude = latitude,
                            longitude = longitude,
                            imageBitmap = selectedImageUri?.let {
                                android.graphics.Bitmap.createBitmap(
                                    it.asAndroidBitmap()
                                )
                            },
                            onResult = { success, messageResId ->
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
                        )
                    } else {
                        errorMessage = context.getString(R.string.error_field_required)
                    }
                }) {
                    Text(stringResource(R.string.save))
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
}