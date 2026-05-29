package com.itis.artistinfodagger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.artistinfodagger.domain.usecase.GetArtistDetailsUseCase
import com.itis.artistinfodagger.presentation.state.DetailsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val useCase: GetArtistDetailsUseCase,
    private val artistId: Int
): ViewModel() {

    init {
        loadArtist()
    }
    private val _state = MutableStateFlow<DetailsScreenState>(DetailsScreenState.Loading)
    val state: StateFlow<DetailsScreenState> = _state

    init {
        loadArtist()
    }

    private fun loadArtist() {
        viewModelScope.launch {
            useCase(artistId)
                .onSuccess { artist ->
                    _state.value = DetailsScreenState.Success(artist)
                }
                .onFailure { e ->
                    _state.value = DetailsScreenState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun retry() {
        loadArtist()
    }


}