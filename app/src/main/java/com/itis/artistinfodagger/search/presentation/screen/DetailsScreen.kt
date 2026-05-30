package com.itis.artistinfodagger.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.itis.artistinfodagger.R
import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.presentation.model.ArtistUiModel
import com.itis.artistinfodagger.presentation.state.DetailsScreenState
import com.itis.artistinfodagger.presentation.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    imageLoader: ImageLoader
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.details_title)) })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val s = state) {
                is DetailsScreenState.Loading -> LoadingContent()
                is DetailsScreenState.Success -> ArtistDetails(artist = s.artist, imageLoader = imageLoader)
                is DetailsScreenState.Error -> ErrorContent(
                    message = s.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.retry_button))
        }
    }
}

@Composable
fun ArtistDetails(
    artist: ArtistUiModel,
    imageLoader: ImageLoader
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val imageUrl = remember(artist.bannerUrl, artist.imageUrl){
            artist.bannerUrl ?: artist.imageUrl
        }

        val stableImageLoader = remember(imageLoader) { imageLoader }
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.artist_image_description),
            imageLoader = stableImageLoader,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = artist.name ?: stringResource(R.string.unknown_artist),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        artist.genre?.let {
            DetailRow(label = stringResource(R.string.genre_label), value = it)
        }
        artist.style?.let {
            DetailRow(label = stringResource(R.string.style_label), value = it)
        }
        artist.formedYear?.let {
            DetailRow(label = stringResource(R.string.formed_year_label), value = it.toString())
        }

        Spacer(modifier = Modifier.height(20.dp))

        artist.biography?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}