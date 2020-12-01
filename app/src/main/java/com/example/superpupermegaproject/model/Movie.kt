package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.R

data class Movie(
    val id: Long,
    val name: String,
    val tags: List<String>,
    val reviewersCount: Int,
    val storyline: String,
    val actors: List<Actor>,
    val ageLimit: Int,
    val listImageID: Int,
    val detailImageID: Int = listImageID,
    val lengthOfMovie: Int,
    var isLiked: Boolean = false,
    var rating: Float = 0F
) {
    val tagsString: String
    get() = tags.joinToString()

    val reviewersCountString: String
    get() = if(reviewersCount==0) {
        "NO REVIEWS"
    } else {
        "$reviewersCount REVIEWS"
    }

    val ageLimitString: String
    get() = if(ageLimit==0) {
        ""
    } else {
        "$ageLimit+"
    }

    val lengthOfMovieString: String
    get() = if(lengthOfMovie==0) {
        ""
    } else {
        "$lengthOfMovie MIN"
    }
}

inline fun getDefaultActors() = listOf(
    Actor(name = "Robert Downey Jr.", imageID = R.drawable.robert_downy_jr),
    Actor(name = "Chris Evans", imageID = R.drawable.chris_evans),
    Actor(name = "Mark Ruffalo", imageID = R.drawable.mark_ruffalo),
    Actor(name = "Chris Hemsworth", imageID = R.drawable.chris_hemsworth)
)