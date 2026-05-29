package com.itis.artistinfodagger.presentation.state

import com.itis.artistinfodagger.data.models.ArtistDto

sealed class SearchScreenState {

    data object Idle : SearchScreenState()

    data object Loading: SearchScreenState()

    data class Success(
        val artists: List<ArtistDto>
    ): SearchScreenState()

    data class Error(
        val message: String
    ): SearchScreenState()
}