package com.example.studyproject_5.presentation.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.studyproject_5.R
import com.example.studyproject_5.presentation.navigation.Screen
import com.example.studyproject_5.presentation.viewmodel.FeedViewModel
import com.example.studyproject_5.presentation.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showPostDetails by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<FeedViewModel.PostItem?>(null) }

    val feedViewModel: FeedViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )

    val posts by feedViewModel.posts.collectAsState()
    val isLoading by feedViewModel.isLoading.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        feedViewModel.loadAllPosts()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.feed))
                        Button(
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(stringResource(R.string.sort_by))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (posts.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_posts),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(posts) { post ->
                            PostCard(
                                post = post,
                                onClick = {
                                    selectedPost = post
                                    showPostDetails = true
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate(Screen.Profile.route) },
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Text("ЛК")
            }

            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddPost.route) },
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Text("+")
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            feedViewModel.loadAllPosts()
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.all_posts))
                    }

                    Button(
                        onClick = {
                            feedViewModel.filterByHemisphere(isNorthern = true)
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.northern_hemisphere))
                    }

                    Button(
                        onClick = {
                            feedViewModel.filterByHemisphere(isNorthern = false)
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.southern_hemisphere))
                    }
                }
            }
        }

        if (showPostDetails && selectedPost != null) {
            ModalBottomSheet(
                onDismissRequest = { showPostDetails = false },
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = selectedPost!!.title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(text = "Страна: ${selectedPost!!.country}")
                    Text(text = "Широта: ${selectedPost!!.latitude}")
                    Text(text = "Долгота: ${selectedPost!!.longitude}")
                    Text(text = "Автор ID: ${selectedPost!!.authorId}")
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: FeedViewModel.PostItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = post.imagePath),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = post.country)
                    Text(text = "ID: ${post.authorId}")
                }
            }
        }
    }
}