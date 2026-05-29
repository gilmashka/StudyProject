package com.itis.artistinfodagger.presentation.state

import com.itis.artistinfodagger.data.models.ArtistDto

sealed class DetailsScreenState {

    data object Loading: DetailsScreenState()

    data class Success(
        val artist: ArtistDto
    ): DetailsScreenState()

    data class Error(
        val message: String
    ): DetailsScreenState()
}