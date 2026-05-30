package com.itis.artistinfodagger.presentation.viewmodel

import com.itis.artistinfodagger.domain.usecase.GetArtistDetailsUseCase
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase
) {

    fun create(artistId: Int): DetailsViewModel{
        return DetailsViewModel(getArtistDetailsUseCase, artistId)
    }
}