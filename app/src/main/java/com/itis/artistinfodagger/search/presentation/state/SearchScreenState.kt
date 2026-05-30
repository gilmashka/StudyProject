package com.itis.artistinfodagger.presentation.state

import com.itis.artistinfodagger.presentation.model.ArtistUiModel

sealed class SearchScreenState {

    data object Idle : SearchScreenState()

    data object Loading: SearchScreenState()

    data class Success(
        val artists: List<ArtistUiModel>
    ): SearchScreenState()

    data class Error(
        val message: String
    ): SearchScreenState()
}