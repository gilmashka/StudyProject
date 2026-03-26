package com.artistinfo.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artistinfo.domain.models.Artist
import androidx.compose.foundation.clickable

@Composable
fun ArtistInfoDivider(
    modifier: Modifier = Modifier.padding(horizontal = 10.dp),
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = MaterialTheme.colorScheme.outline.copy(0.25f)
    )
}

@Composable
fun ArtistInfoCircularProgressIndicator(
    modifier: Modifier = Modifier
){
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 4.dp

    )
}

@Composable
fun ArtistCard(artist: Artist, onClick: () -> Unit){
    Column(
        modifier = Modifier.width(380.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(35.dp))
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(35.dp)
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                all = 10.dp
            )

    ) {
        Text(
            text = artist.name,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.width(200.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = artist.genre,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.width(200.dp)
        )
    }
}

@Composable
fun ArtistInfoText(label: String, value: String?) {
    Text(
        text = if(value != null){"$label: $value"}else{"$label: - "},
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.width(380.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ArtistInfoText(label: String, value: Int?) {
    Text(
        text = if(value != null){"$label: $value"}else{"$label: - "},
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.width(380.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

