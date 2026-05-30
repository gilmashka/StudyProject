package com.itis.artistinfodagger.presentation.utils

import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.presentation.model.ArtistUiModel

fun ArtistDto.toUiModel(): ArtistUiModel {
    return ArtistUiModel(
        id = this.idArtist,
        name = this.strArtist ?: "Unknown Artist",
        genre = this.strGenre,
        style = this.strStyle,
        formedYear = this.intFormedYear,
        biography = this.strBiography,
        imageUrl = this.strArtistThumb,
        bannerUrl = this.strArtistBanner
    )
}

fun List<ArtistDto>.toUiModelList(): List<ArtistUiModel> {
    return this.map { it.toUiModel() }
}