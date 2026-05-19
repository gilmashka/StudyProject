package com.itis.artistinfodagger.data.repositoryImpls

import com.itis.artistinfodagger.api.TheAudioDBApi
import com.itis.artistinfodagger.data.models.ArtistDto
import com.itis.artistinfodagger.data.models.TheAudioDBResponse
import com.itis.artistinfodagger.domain.repository.ArtistInfoRepository
import javax.inject.Inject

class ArtistInfoRepositoryImpl @Inject constructor(
    private val api: TheAudioDBApi
): ArtistInfoRepository {

    override suspend fun getSearchRequest(query: String): Result<TheAudioDBResponse> {
        return try {
            val response = api.getSearchRequest(query)

            if (response.isSuccessful) {
                Result.success(value = response.body() ?: TheAudioDBResponse(null))
            } else if ( response.code() == 404) {
                Result.success(TheAudioDBResponse(emptyList()))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistDetails(id: Int): Result<ArtistDto> {
        return try {
            val response = api.getArtistInfo(id)

            if (response.isSuccessful) {
                val artist = response.body()?.artists?.firstOrNull()
                if (artist != null){
                    Result.success(value = artist)
                } else {
                    Result.failure(Exception("Артист не найден"))
                }
            } else if ( response.code() == 404) {
                Result.failure(Exception("Артист не найден"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}