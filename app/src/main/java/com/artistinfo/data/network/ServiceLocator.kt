package com.artistinfo.network

import com.artistinfo.data.repository.ArtistRepositoryImpl
import com.artistinfo.domain.repositories.ArtistRepository
import java.util.concurrent.TimeUnit
import com.artistinfo.network.dto_and_response_objects.TheAudioDBApi
import com.artistinfo.repository.mappers.ArtistMapper
import com.artistinfo.usecase.SearchArtistUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceLocator {

    private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/2/"

    private val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    private val theAudioDBApi = retrofit.create(TheAudioDBApi::class.java)

    fun getTheAudioDBApi() = theAudioDBApi
    private val artistMapper = ArtistMapper()

    fun getArtistRepository() : ArtistRepository {
        return ArtistRepositoryImpl(
            theAudioDBApi = getTheAudioDBApi(),
            artistMapper = artistMapper
        )
    }

    fun getSearchArtistUseCase() : SearchArtistUseCase {
        return SearchArtistUseCase(
            artistRepository = getArtistRepository()
        )
    }
}