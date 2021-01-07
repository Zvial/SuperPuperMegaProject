package com.example.superpupermegaproject.model

import kotlinx.serialization.*

@Serializable
data class GenresResponse (
    val genres: List<GenreResponse>
)

@Serializable
data class GenreResponse (
    val id: Long,
    val name: String
)