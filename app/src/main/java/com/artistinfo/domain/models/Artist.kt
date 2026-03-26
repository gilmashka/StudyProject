package com.artistinfo.domain.models

class Artist(
    val name: String,
    val genre: String,
    val style: String,
    val formedYear: Int,
    val diedYear: Int?,
    val mood: String,
    val label: String?,
    val website: String?,
    val biography: String?
) {}