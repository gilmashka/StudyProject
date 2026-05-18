package com.artistinfo.presentation.states

import com.artistinfo.domain.models.Artist

sealed class ArtistUiState {
    object Initial : ArtistUiState()
    object Loading : ArtistUiState()
    object Empty : ArtistUiState()
    data class Success(val artists: List<Artist>) : ArtistUiState()
    data class Error(val message: String) : ArtistUiState()
}