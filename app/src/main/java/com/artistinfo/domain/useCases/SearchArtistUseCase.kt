package com.artistinfo.usecase

import com.artistinfo.domain.models.Artist
import com.artistinfo.domain.repositories.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class SearchArtistUseCase (
    private val artistRepository: ArtistRepository
) {


    suspend operator fun invoke ( query: String ) : List<Artist> {
        return withContext(Dispatchers.IO){
            artistRepository.getArtistInfoByQuery(query)
        }
    }
}