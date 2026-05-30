package com.itis.artistinfodagger.api

import com.itis.artistinfodagger.data.models.TheAudioDBResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheAudioDBApi {


    @GET("search.php")
    suspend fun getSearchRequest(
        @Query(value = "s") query: String
    ) : Response<TheAudioDBResponse>

    @GET("artist.php")
    suspend fun getArtistInfo(
        @Query("i") id: Int
    ): Response<TheAudioDBResponse>
}