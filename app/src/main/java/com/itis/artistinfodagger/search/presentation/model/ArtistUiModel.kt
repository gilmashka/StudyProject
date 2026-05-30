package com.itis.artistinfodagger.presentation.model

data class ArtistUiModel(
    val id: Int,
    val name: String,
    val genre: String?,
    val style: String?,
    val formedYear: Int?,
    val biography: String?,
    val imageUrl: String?,
    val bannerUrl: String?
)