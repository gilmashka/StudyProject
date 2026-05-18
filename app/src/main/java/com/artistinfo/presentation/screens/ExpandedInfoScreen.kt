package com.artistinfo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.artistinfo.domain.models.Artist
import com.artistinfo.presentation.ui.components.ArtistInfoDivider
import com.artistinfo.presentation.ui.components.ArtistInfoPrimaryIconButton
import com.artistinfo.presentation.ui.components.ArtistInfoText
import com.artistinfo.R

@Composable
fun ExpandedInfoScreen(
    artist: Artist,
    navController: NavController
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(10.dp))

                ArtistInfoPrimaryIconButton(
                    icon = Icons.Rounded.ArrowBackIosNew,
                    onClick = {navController.popBackStack()}
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            ArtistInfoDivider()

            Spacer(modifier = Modifier.height(20.dp))

            ArtistInfoText(stringResource(R.string.genre), artist.genre)

            ArtistInfoText(stringResource(R.string.style), artist.style)

            ArtistInfoText(stringResource(R.string.label), artist.label)

            ArtistInfoText(stringResource(R.string.mood), artist.mood)

            ArtistInfoText(stringResource(R.string.formed_year), artist.formedYear)

            ArtistInfoText(stringResource(R.string.dead_year), artist.diedYear)

            ArtistInfoText(stringResource(R.string.website), artist.website)

            Spacer(modifier = Modifier.height(10.dp))

            ArtistInfoDivider()

            Spacer(modifier = Modifier.height(10.dp))

            ArtistInfoText(stringResource(R.string.biography), artist.biography)

        }
    }
}