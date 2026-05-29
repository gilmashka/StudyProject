package com.itis.artistinfodagger.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.itis.artistinfodagger.R
import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.presentation.state.SearchScreenState
import com.itis.artistinfodagger.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onArtistClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.search_title)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { viewModel.searchArtist(query) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (val s = state) {
                    is SearchScreenState.Idle -> EmptyContent()
                    is SearchScreenState.Loading -> LoadingContent()
                    is SearchScreenState.Success -> ArtistList(
                        artists = s.artists,
                        onClick = onArtistClick,
                        imageLoader = imageLoader
                    )
                    is SearchScreenState.Error -> ErrorContent(
                        message = s.message,
                        onRetry = { viewModel.searchArtist(query) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { throw RuntimeException("Test crash") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.crash_button))
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.enter_query),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
fun SearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            placeholder = { Text(stringResource(R.string.search_placeholder)) }
        )
        Button(
            onClick = onSearch,
            modifier = Modifier.padding(start = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(stringResource(R.string.search_button))
        }
    }
}

@Composable
fun ArtistList(
    artists: List<ArtistDto>,
    onClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    LazyColumn {
        items(artists) { artist ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { artist.idArtist?.let(onClick) }
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = artist.strArtistThumb,
                        contentDescription = stringResource(R.string.artist_thumbnail_description),
                        imageLoader = imageLoader,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = artist.strArtist ?: stringResource(R.string.unknown_artist),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}