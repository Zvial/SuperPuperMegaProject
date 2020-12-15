package com.example.superpupermegaproject.data

import com.android.academy.fundamentals.homework.features.data.Movie

object MoviesDataSource {
    var moviesList: List<Movie> = listOf()

    fun setRating(movieID: Long, rating: Float) {
        getMovieByID(movieID)?.ratings = rating *2
    }

    fun setLiked(movieID: Long, isLiked: Boolean) {
        getMovieByID(movieID)?.isLiked = isLiked
    }

    @Deprecated(message = "not used")
    fun shuffleList() {
        //moviesList.shuffle()
    }

    fun getMovieByID(movieID: Long) = moviesList.firstOrNull {
        it.id == movieID
    }
}