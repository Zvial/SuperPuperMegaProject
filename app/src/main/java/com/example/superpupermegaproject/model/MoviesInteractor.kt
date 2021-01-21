package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.data.Actor
import com.example.superpupermegaproject.data.Genre
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.api_responses.GenreResponse
import com.example.superpupermegaproject.model.api_responses.MovieDetailResponse
import com.example.superpupermegaproject.model.api_responses.MovieItemResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesInteractor private constructor(
        private val repository: Repository
) {
    private val genresCache: MutableList<Genre> = mutableListOf()
    private val runtimesCache = mutableMapOf<Int, Int>()
    private val mapper = Mapper()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadGenres()
        }
    }

    suspend fun searchMovies(
            query: String,
            isNewQuery: Boolean,
            loadRuntimeValue: Boolean = false
    ): List<Movie> {
        if (isNewQuery) repository.clearLastPage()
        return repository.searchMovies(query).map { movieResponse ->
            mapper.createMovieInstance(movieResponse, loadRuntimeValue)
        }
    }

    suspend fun getMovies(
            isNewQuery: Boolean,
            loadRuntimeValue: Boolean = false
    ): List<Movie> {
        if (isNewQuery) repository.clearLastPage()
        return repository.loadMovies().map { movieResponse ->
            mapper.createMovieInstance(movieResponse, loadRuntimeValue)
        }
    }

    suspend fun getMovie(id: Int): Movie? {
        val movieResponse = repository.loadMovie(id)
        if (movieResponse == null)
            return null

        return mapper.createMovieInstance(movieResponse, true)
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
            mapper.createGenreInstance(it)
        })
    }

    inner class Mapper {
        suspend fun createMovieInstance(from: MovieItemResponse, loadRuntimeValue: Boolean): Movie =
                Movie(
                        from.id,
                        from.title,
                        from.overview,
                        poster = from.posterPath ?: "",
                        backdrop = from.backdropPath ?: "",
                        from.voteAverage.toFloat(),
                        from.adult,
                        if (loadRuntimeValue) getRuntimeValueForMovie(from.id.toInt()) else 0,
                        genresCache.filter { genre ->
                            from.genreIDS.contains(genre.id.toLong())
                        },
                        listOf(),
                        from.voteCount.toInt(),
                        false
                )

        suspend fun createMovieInstance(from: MovieDetailResponse, loadRuntimeValue: Boolean): Movie {
            val genres = from.genres.map { genreResponse ->
                createGenreInstance(genreResponse)
            }

            return Movie(
                    from.id,
                    from.title,
                    from.overview,
                    poster = from.posterPath ?: "",
                    backdrop = from.backdropPath ?: "",
                    from.voteAverage.toFloat(),
                    from.adult,
                    from.runtime.toInt(),
                    genres,
                    getMovieActors(from.id.toInt()),
                    from.voteCount.toInt(),
                    false
            )
        }

        suspend fun createGenreInstance(from: GenreResponse): Genre = Genre(from.id.toInt(), from.name)
    }

    companion object {
        fun getInstance(repository: Repository) = MoviesInteractor(repository)
    }
}