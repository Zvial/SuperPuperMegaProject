package com.example.superpupermegaproject.model

import kotlinx.serialization.*

@Serializable
data class MovieDetailResponse (
    val adult: Boolean,

    @SerialName("backdrop_path")
    var backdropPath: String,

    //@SerialName("belongs_to_collection")
    //val belongsToCollection: Any? = null,

    val budget: Long,
    val genres: List<GenreResponse>,
    val homepage: String,
    val id: Long,

    @SerialName("imdb_id")
    val imdbID: String,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    val overview: String,
    val popularity: Double,

    @SerialName("poster_path")
    var posterPath: String? = null,

    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>,

    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,

    @SerialName("release_date")
    val releaseDate: String,

    val revenue: Long,
    val runtime: Long,

    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Long
)

@Serializable
data class ProductionCompany (
    val id: Long,

    @SerialName("logo_path")
    val logoPath: String? = null,

    val name: String,

    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class ProductionCountry (
    @SerialName("iso_3166_1")
    val iso3166_1: String,

    val name: String
)

@Serializable
data class SpokenLanguage (
    @SerialName("iso_639_1")
    val iso639_1: String,

    val name: String
)