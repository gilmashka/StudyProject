package com.itis.artistinfodagger.data.models

import com.google.gson.annotations.SerializedName
data class ArtistDto(
    @SerializedName("idArtist")
    val idArtist: Int,

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
    val strBiography: String?,

    @SerializedName("strArtistThumb")
    val strArtistThumb: String?,

    @SerializedName("strArtistLogo")
    val strArtistLogo: String?,

    @SerializedName("strArtistBanner")
    val strArtistBanner: String?
)