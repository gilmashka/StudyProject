package com.itis.artistinfodagger.presentation.state

import com.itis.artistinfodagger.presentation.model.ArtistUiModel

sealed class DetailsScreenState {

    data object Loading: DetailsScreenState()

    data class Success(
        val artist: ArtistUiModel
    ): DetailsScreenState()

    data class Error(
        val message: String
    ): DetailsScreenState()
}