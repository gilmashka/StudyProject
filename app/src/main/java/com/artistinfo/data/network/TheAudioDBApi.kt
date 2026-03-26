package com.artistinfo.network.dto_and_response_objects

import com.artistinfo.network.TheAudioDBResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TheAudioDBApi {

    @GET("search.php")
    suspend fun getArtistDataFromQuery(
        @Query(value = "s" )
        query: String): TheAudioDBResponse
}