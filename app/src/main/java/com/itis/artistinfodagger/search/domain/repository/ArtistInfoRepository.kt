package com.itis.artistinfodagger.domain.repository

import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.data.models.TheAudioDBResponse

interface ArtistInfoRepository {

    suspend fun getSearchRequest(query: String): Result<TheAudioDBResponse>

    suspend fun getArtistDetails(id: Int): Result<ArtistDto>
}