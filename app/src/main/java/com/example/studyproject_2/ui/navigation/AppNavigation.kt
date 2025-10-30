package com.example.studyproject_2.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginScreenObject

@Serializable
data class NotesScreenObject(
    val email: String
)

@Serializable
data object AddNoteScreenObject