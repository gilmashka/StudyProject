package com.artistinfo.domain.repositories

import com.artistinfo.domain.models.Artist

interface ArtistRepository {

    suspend fun getArtistInfoByQuery (query:String) : List<Artist>
}