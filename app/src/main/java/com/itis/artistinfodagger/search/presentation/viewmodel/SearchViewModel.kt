package com.itis.artistinfodagger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.artistinfodagger.domain.usecase.SearchArtistUseCase
import com.itis.artistinfodagger.presentation.state.SearchScreenState
import com.itis.artistinfodagger.presentation.utils.toUiModelList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: SearchArtistUseCase
): ViewModel() {

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
    val state: StateFlow<SearchScreenState> = _state

    private var lastQuery: String = ""
    fun searchArtist(query: String) {
        if (query.isBlank()) return

        lastQuery = query
        _state.value = SearchScreenState.Loading
        viewModelScope.launch {
            useCase(query)
                .onSuccess { result ->
                    val artists = result.artists?.toUiModelList() ?: emptyList()
                    _state.value = SearchScreenState.Success(artists = artists)
                }
                .onFailure { e ->
                    _state.value = SearchScreenState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun retryLastSearch() {
        if (lastQuery.isNotBlank()) {
            searchArtist(lastQuery)
        }
    }
}