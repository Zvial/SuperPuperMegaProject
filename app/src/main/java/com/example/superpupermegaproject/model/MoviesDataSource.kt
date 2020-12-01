package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.R

object MoviesDataSource {
    val moviesList = listOf(
        //1
        Movie(
            id = 1,
            name = "Avengers: End Game",
            tags = listOf("Action", "Adventure", "Fantasy"),
            reviewersCount = 125,
            storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            ageLimit = 13,
            listImageID = R.drawable.movie_avangers_end_game,
            detailImageID = R.drawable.movie_avangers_end_game_detail,
            rating = 4f,
            actors = getDefaultActors(),
            lengthOfMovie = 137
        ),
        //2
        Movie(
            id = 2,
            name = "Tenet",
            tags = listOf("Action", "Sci-Fi", "Thriller"),
            reviewersCount = 98,
            storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            ageLimit = 16,
            listImageID = R.drawable.movie_tenet,
            rating = 5f,
            actors = getDefaultActors(),
            isLiked = true,
            lengthOfMovie = 97
        ),
        //3
        Movie(
            id = 4,
            name = "Black Widow",
            tags = listOf("Action", "Adventure", "Sci-Fi"),
            reviewersCount = 38,
            storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            ageLimit = 13,
            listImageID = R.drawable.movie_black_widow,
            rating = 4f,
            actors = getDefaultActors(),
            lengthOfMovie = 102
        ),
        //4
        Movie(
            id = 3,
            name = "Wonder Woman 1984",
            tags = listOf("Action", "Adventure", "Fantasy"),
            reviewersCount = 74,
            storyline = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            ageLimit = 13,
            listImageID = R.drawable.movie_wonder_woman,
            rating = 5f,
            actors = getDefaultActors(),
            lengthOfMovie = 120
        )
    )

    fun setRating(movieID: Long, rating: Float) {
        getMovieByID(movieID)?.rating = rating
    }

    fun setLiked(movieID: Long, isLiked: Boolean) {
        getMovieByID(movieID)?.isLiked = isLiked
    }

    private fun getMovieByID(movieID: Long) = moviesList.firstOrNull{
        it.id == movieID
    }
}