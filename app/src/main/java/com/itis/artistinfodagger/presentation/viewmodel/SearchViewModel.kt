package com.itis.artistinfodagger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.artistinfodagger.domain.usecase.SearchArtistUseCase
import com.itis.artistinfodagger.presentation.state.SearchScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: SearchArtistUseCase
): ViewModel() {

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
    val state: StateFlow<SearchScreenState> = _state

    fun searchArtist(query: String) {
        if (query.isBlank()) return

        _state.value = SearchScreenState.Loading
        viewModelScope.launch {
            useCase(query)
                .onSuccess { result ->
                    val artists = result.artists ?: emptyList()
                    android.util.Log.d("ARTIST_DEBUG", "Найдено: ${artists.size}")
                    artists.forEach { artist ->
                        android.util.Log.d("ARTIST_DEBUG", "Имя: ${artist.strArtist}")
                        android.util.Log.d("ARTIST_DEBUG", "Thumb: ${artist.strArtistThumb}")
                        android.util.Log.d("ARTIST_DEBUG", "Banner: ${artist.strArtistBanner}")
                    }
                    _state.value = SearchScreenState.Success(artists = artists)
                }
                .onFailure { e ->
                    _state.value = SearchScreenState.Error(e.message ?: "Unknown error")
                }
        }
    }

}