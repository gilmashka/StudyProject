package com.itis.artistinfodagger.data.models

import com.google.gson.annotations.SerializedName

data class TheAudioDBResponse(
    @SerializedName("artists")
    val artists: List<ArtistDto>?
)