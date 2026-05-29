package com.itis.artistinfodagger.domain.usecase

import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import javax.inject.Inject

class GetArtistDetailsUseCase @Inject constructor(
    private val repository: ArtistInfoRepository
) {

    suspend operator fun invoke(id: Int): Result<ArtistDto> {
        return repository.getArtistDetails(id)
    }
}