package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.data.Actor
import com.example.superpupermegaproject.data.Genre
import com.example.superpupermegaproject.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesInteractor private constructor(
        private val repository: Repository
) {
    private val genresCache: MutableList<Genre> = mutableListOf()
    private val runtimesCache = mutableMapOf<Int, Int>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadGenres()
        }
    }

    suspend fun getMovies(page: Int, loadRuntimeValue: Boolean = false): List<Movie> =
            repository.loadMovies(page).map { movieResponse ->
                Movie(
                        movieResponse.id,
                        movieResponse.title,
                        movieResponse.overview,
                        poster = movieResponse.posterPath,
                        backdrop = movieResponse.backdropPath,
                        movieResponse.voteAverage.toFloat(),
                        movieResponse.adult,
                        if (loadRuntimeValue) getRuntimeValueForMovie(movieResponse.id.toInt()) else 0,
                        genresCache.filter { genre ->
                            movieResponse.genreIDS.contains(genre.id.toLong())
                        },
                        listOf(),
                        movieResponse.voteCount.toInt(),
                        false
                )
            }

    suspend fun getMovie(id: Int): Movie? {
        val movieResponse = repository.loadMovie(id)
        if (movieResponse == null)
            return null

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

    private suspend fun getRuntimeValueForMovie(movieID: Int) =
            if (runtimesCache.containsKey(movieID)) {
                runtimesCache[movieID]!!
            } else {
                val value = repository.loadMovie(movieID)?.runtime?.toInt() ?: 0
                runtimesCache.put(movieID, value)
                value
            }

    private suspend fun getMovieActors(movieID: Int) = repository.loadMovieActors(movieID)
            .map { castResponse ->
                Actor(
                        id = castResponse.id.toInt(),
                        name = castResponse.name,
                        picture = castResponse.profilePath
                )
            }

    private suspend fun loadGenres() {
        genresCache.addAll(repository.loadGenres().map {
            Genre(it.id.toInt(), it.name)
        })
    }

    companion object {
        fun getInstance(repository: Repository) = MoviesInteractor(repository)
    }
}