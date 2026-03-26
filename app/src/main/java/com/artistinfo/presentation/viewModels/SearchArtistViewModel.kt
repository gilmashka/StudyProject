package com.artistinfo.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artistinfo.presentation.states.ArtistUiState
import com.artistinfo.usecase.SearchArtistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchArtistViewModel (
    private val searchArtistUseCase: SearchArtistUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<ArtistUiState>(ArtistUiState.Initial)
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun updateQuery(newQuery: String){
        _query.value = newQuery
    }

    fun search(){
        val currentQuery = _query.value
        if(currentQuery.isBlank()){
            return
        }

        viewModelScope.launch {
            _uiState.value = ArtistUiState.Loading

            val artists = try {
                searchArtistUseCase(currentQuery)
            } catch (e: HttpException){
                when(e.code()){
                    404 -> _uiState.value = ArtistUiState.Error("404: Not Found")
                    in 400 .. 499 -> _uiState.value = ArtistUiState.Error("Client Error, code ${e.code()}")
                    in 500 .. 599 -> _uiState.value = ArtistUiState.Error("Server Error, code ${e.code()}")
                    else -> _uiState.value = ArtistUiState.Error("Unexpected network error, code ${e.code()}")
                }
                return@launch
            } catch (e: Exception) {
                _uiState.value = ArtistUiState.Error(e.message ?: "Error")
                return@launch
            }

            _uiState.value = if (artists.isEmpty()) {
                ArtistUiState.Empty
            } else {
                ArtistUiState.Success(artists)
            }
        }
    }
}