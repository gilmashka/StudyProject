package com.artistinfo.data.repository

import com.artistinfo.domain.models.Artist
import com.artistinfo.network.dto_and_response_objects.TheAudioDBApi
import com.artistinfo.domain.repositories.ArtistRepository
import com.artistinfo.repository.mappers.ArtistMapper
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class ArtistRepositoryImpl(
    private val theAudioDBApi: TheAudioDBApi,
    private val artistMapper: ArtistMapper
    ) : ArtistRepository {


    override suspend fun getArtistInfoByQuery(query:String) : List<Artist> {

        if(query == "show_404"){
            throw HttpException(
                Response.error<Any>(
                    404,
                    "Not Found".toResponseBody(null)
                )
            )
        } else{
            val response = theAudioDBApi.getArtistDataFromQuery(query).artists
            return artistMapper.mapArtistList(response)
        }

    }
}