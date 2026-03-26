package com.artistinfo.network.dto_and_response_objects

import com.google.gson.annotations.SerializedName

data class ArtistDTO(
    @SerializedName("idArtist")
    val idArtist: Int?,

    @SerializedName("strArtist")
    val strArtist: String?,

    @SerializedName("strGenre")
    val strGenre: String?,

    @SerializedName("strStyle")
    val strStyle: String?,

    @SerializedName("intFormedYear")
    val intFormedYear: Int?,

    @SerializedName("intDiedYear")
    val intDiedYear: Int?,

    @SerializedName("strMood")
    val strMood: String?,

    @SerializedName("strLabel")
    val strLabel: String?,

    @SerializedName("strWebsite")
    val strWebsite: String?,

    @SerializedName("strBiography")
    val strBiography: String?

    ) {}