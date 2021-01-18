package com.example.superpupermegaproject.model.api_responses

import kotlinx.serialization.*

@Serializable
data class MoviesListResponse (
    val page: Long,
    val results: List<MovieItemResponse>,

    @SerialName("total_pages")
    val totalPages: Long,

    @SerialName("total_results")
    val totalResults: Long
)

@Serializable
data class MovieItemResponse(
    val adult: Boolean,

    @SerialName("backdrop_path")
    var backdropPath: String?,

    @SerialName("genre_ids")
    val genreIDS: List<Long>,

    val id: Long,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    val overview: String,
    val popularity: Double,

    @SerialName("poster_path")
    var posterPath: String? = null,

    @SerialName("release_date")
    val releaseDate: String? = null,

    val title: String,
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Long
)