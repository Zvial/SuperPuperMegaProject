package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.data.Actor
import com.example.superpupermegaproject.data.Genre
import com.example.superpupermegaproject.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository private constructor(private val ApiInteractor: RESTApiInteractor) {
    private val genresCache: MutableList<Genre> = mutableListOf()
    private val runtimesCache = mutableMapOf<Int, Int>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadGenres()
        }
    }

    suspend fun getMovies(page: Int): List<Movie> =
        ApiInteractor.loadMovies(page).map { movieResponse ->
            Movie(
                movieResponse.id,
                movieResponse.title,
                movieResponse.overview,
                poster = movieResponse.posterPath,
                backdrop = movieResponse.backdropPath,
                movieResponse.voteAverage.toFloat(),
                movieResponse.adult,
                getRuntimeValueForMovie(movieResponse.id.toInt()),
                genresCache.filter { genre ->
                    movieResponse.genreIDS.contains(genre.id.toLong())
                },
                listOf(),
                movieResponse.voteCount.toInt(),
                false
            )
        }

    suspend fun getMovie(id: Int): Movie {
        val movieResponse = ApiInteractor.loadMovie(id)
        val genres = movieResponse.genres.map { genreResponse ->
            Genre(genreResponse.id.toInt(), genreResponse.name)
        }
        return Movie(
            movieResponse.id,
            movieResponse.title,
            movieResponse.overview,
            poster = movieResponse.posterPath ?: "",
            backdrop = movieResponse.backdropPath,
            movieResponse.voteAverage.toFloat(),
            movieResponse.adult,
            movieResponse.runtime.toInt(),
            genres,
            getMovieActors(movieResponse.id.toInt()),
            movieResponse.voteCount.toInt(),
            false
        )
    }

    private suspend fun getRuntimeValueForMovie(movieID: Int) = if(runtimesCache.containsKey(movieID)) {
        runtimesCache[movieID]!!
    } else {
        val value = ApiInteractor.loadMovie(movieID).runtime.toInt()
        runtimesCache.put(movieID, value)
        value
    }

    private suspend fun getMovieActors(movieID: Int) = ApiInteractor.loadMovieActors(movieID)
        .map { castResponse ->
            Actor(
                id = castResponse.id.toInt(),
                name = castResponse.name,
                picture = castResponse.profilePath
            )
        }

    private suspend fun loadGenres() {
        genresCache.addAll(ApiInteractor.loadGenres().map {
            Genre(it.id.toInt(), it.name)
        })
    }

    companion object {
        fun getInstance(ApiInteractor: RESTApiInteractor) = Repository(ApiInteractor)
    }
}