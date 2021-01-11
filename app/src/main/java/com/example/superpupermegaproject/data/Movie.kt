package com.example.superpupermegaproject.data

data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val poster: String?,
    val backdrop: String?,
    var ratings: Float,
    val adult: Boolean,
    val runtime: Int,
    val genres: List<Genre>,
    val actors: List<Actor>,
    val voteCount: Int = 0,
    var isLiked: Boolean = false
) {
    val genresString: String
        get() = genres.joinToString { genre->
            genre.name
        }

    val voteCountString: String
        get() = if(voteCount==0) {
            "NO REVIEWS"
        } else {
            "$voteCount REVIEWS"
        }

    val ageLimitString: String
        get() = if(adult) {
            ""
        } else {
            "13+"
        }

    val runtimeString: String
        get() = if(runtime==0) {
            ""
        } else {
            "$runtime MIN"
        }

    val voteRating: Float
        get() = ratings / 2
}