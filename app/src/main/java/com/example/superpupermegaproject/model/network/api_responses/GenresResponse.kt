package com.example.superpupermegaproject.model.network.api_responses

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