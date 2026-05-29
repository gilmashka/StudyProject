package com.itis.artistinfodagger.domain.usecase

import com.itis.artistinfodagger.data.models.TheAudioDBResponse
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import javax.inject.Inject

class SearchArtistUseCase @Inject constructor(
    private val repository: ArtistInfoRepository
){

    suspend operator fun invoke(query: String): Result<TheAudioDBResponse> {
        return repository.getSearchRequest(query)
    }
}