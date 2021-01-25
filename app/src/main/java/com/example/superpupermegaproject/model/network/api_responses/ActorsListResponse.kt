package com.example.superpupermegaproject.model.network.api_responses

import kotlinx.serialization.*

@Serializable
data class CreditsListResponse (
    val id: Long,
    val cast: List<CastResponse>,
    val crew: List<CastResponse>
)

@Serializable
data class CastResponse (
    val adult: Boolean,
    val gender: Long,
    val id: Long,

    val name: String,

    @SerialName("original_name")
    val originalName: String,

    val popularity: Double,

    @SerialName("profile_path")
    var profilePath: String? = null,

    @SerialName("cast_id")
    val castID: Long? = null,

    val character: String? = null,

    @SerialName("credit_id")
    val creditID: String,

    val order: Long? = null,
    val job: String? = null
)