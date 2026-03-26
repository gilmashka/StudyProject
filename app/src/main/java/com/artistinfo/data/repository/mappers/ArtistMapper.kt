package com.artistinfo.repository.mappers

import com.artistinfo.domain.models.Artist
import com.artistinfo.network.dto_and_response_objects.ArtistDTO

class ArtistMapper{
    val UNKNOWN: String = "Неизвестно"

    fun mapArtistList(artistDTOList: List<ArtistDTO>?) : List<Artist> {
        if(artistDTOList == null || artistDTOList.isEmpty()){
            return emptyList()
        }
        else {
            var artistList = mutableListOf<Artist>()
            for(i: ArtistDTO in artistDTOList){
                artistList.add(mapArtist(i))
            }
            return artistList
        }
    }

    fun mapArtist(artistDTO: ArtistDTO) : Artist {

        return Artist(
            name = artistDTO.strArtist!!,
            genre = artistDTO.strGenre!!,
            style = if(artistDTO.strStyle.isNullOrBlank()){UNKNOWN}else{artistDTO.strStyle},
            formedYear = artistDTO.intFormedYear!!,
            diedYear = artistDTO.intDiedYear,
            mood = if(artistDTO.strMood.isNullOrBlank()){UNKNOWN}else{artistDTO.strMood},
            label = if(artistDTO.strLabel.isNullOrBlank()){UNKNOWN}else{artistDTO.strLabel},
            website = if(artistDTO.strWebsite.isNullOrBlank()){UNKNOWN}else{artistDTO.strWebsite},
            biography = if(artistDTO.strBiography.isNullOrBlank()){UNKNOWN}else{artistDTO.strBiography}
        )
    }

}
