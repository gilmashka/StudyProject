package com.artistinfo.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artistinfo.presentation.viewModels.SearchArtistViewModel
import com.artistinfo.R
import com.artistinfo.domain.models.Artist
import com.artistinfo.presentation.states.ArtistUiState
import com.artistinfo.presentation.ui.components.ArtistCard
import com.artistinfo.presentation.ui.components.ArtistInfoCircularProgressIndicator
import com.artistinfo.presentation.ui.components.ArtistInfoDivider
import com.artistinfo.presentation.ui.components.ArtistInfoSearchField

@Composable
fun SearchScreen(
    viewModel: SearchArtistViewModel,
    onArtistClick: (Artist) -> Unit
    ){

    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.main_label_text),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.width(380.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            ArtistInfoDivider()

            Spacer(Modifier.height(30.dp))

            ArtistInfoSearchField(
                value = query,
                onValueChange = { viewModel.updateQuery(it) },
                placeholder =  stringResource(R.string.placeholder),
                onSearchClick = { viewModel.search() },
                modifier = Modifier.width(380.dp),
            )

            Spacer(modifier = Modifier.height(60.dp))

            //res-ts
            when (uiState) {
                is ArtistUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        ArtistInfoCircularProgressIndicator()
                    }
                }

                is ArtistUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        (uiState as ArtistUiState.Success).artists.forEach { artist ->
                            ArtistCard(
                                artist,
                                onClick = { onArtistClick(artist) }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                is ArtistUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = stringResource(R.string.nothing_searched),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is ArtistUiState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = stringResource(R.string.not_searched_yet),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is ArtistUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(100.dp)
                            .background(MaterialTheme.colorScheme.error),
                        contentAlignment = Alignment.Center,

                    ){
                        Text(
                            text = (uiState as ArtistUiState.Error).message,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
