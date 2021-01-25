package com.example.superpupermegaproject.model

import androidx.paging.DataSource
import com.example.superpupermegaproject.data.Actor
import com.example.superpupermegaproject.data.Genre
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.database.entities.*
import com.example.superpupermegaproject.model.network.api_responses.GenreResponse
import com.example.superpupermegaproject.model.network.api_responses.MovieDetailResponse
import com.example.superpupermegaproject.model.network.api_responses.MovieItemResponse
import com.example.superpupermegaproject.model.network.api_responses.RemoteAPIRepository
import kotlinx.coroutines.*
import timber.log.Timber

class MoviesInteractor private constructor(
        private val remoteAPIRepository: RemoteAPIRepository,
        private val movieDatabase: MoviesDatabase
) {
    private val pageSize = 20
    private val genresCache: MutableList<Genre> = mutableListOf() //кэширование всех доступных жанров
    private val runtimesCache = mutableMapOf<Int, Int>() //кэширование продолжительности фильмов
    private val mapper = Mapper() //Преобразование Movie в типы response/entity и обратно
    private var currentPage: Int = 0 //загружаемая страница

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadGenres()
        }
    }

    //Поиск фильмов по подстроке. Только из внешнего API
    suspend fun searchMovies(
            query: String,
            isNewQuery: Boolean,
            loadRuntimeValue: Boolean = false
    ): List<Movie> {
        if (isNewQuery) remoteAPIRepository.clearLastPage()
        return remoteAPIRepository.searchMovies(query).map { movieResponse ->
            mapper.createMovieInstance(movieResponse, loadRuntimeValue)
        }
    }

    //Получение списка фильмов. Если доступа к внешнему API нет, получает из локального кэша
    suspend fun getMovies(): List<Movie> {
        val result = mutableListOf<Movie>()

        result.addAll(getMoviesFromCache()) //запрос данных для currentPage из кэша

        if (result.isEmpty()) {
            try {
                val movies = loadMoviesFromNet() //запрос данных для currentPage из API
                result.addAll(movies)
            } catch (t: Throwable) {
                Timber.d(t.localizedMessage)
            }
            pushMoviesToCache(result, getBeginRowNumberForCurrentPage()) //помещение в кэш
        } else {
            GlobalScope.launch {
                loadAndPushMoviesToCache() //обновление в кэше в параллельном потоке
            }
        }

        if (!result.isEmpty()) currentPage++

        return result
    }

    //Получение данных фильма из API
    suspend fun getMovie(id: Int): Movie? {
        val movieResponse = remoteAPIRepository.loadMovie(id)
        if (movieResponse == null)
            return null

        return mapper.createMovieInstance(movieResponse, true)
    }

    private suspend fun getMoviesFromCache(): List<Movie> =
            movieDatabase.moviesDAO.getAllByPage(getBeginRowNumberForCurrentPage(), pageSize).map {
                mapper.createMovieInstance(it)
            }

    private suspend fun pushMoviesToCache(movies: List<Movie>, startPosition: Int) {
        var curPosition = startPosition
        movieDatabase.moviesDAO.insertAll(movies.map {
            mapper.createMovieEntityInstance(it, ++curPosition)
        },
                movies.flatMap { movie ->
                    movie.genres.map { genre ->
                        MoviesGenresEntity(
                                movieID = movie.id,
                                genreID = genre.id.toLong())
                    }
                })
    }

    private suspend fun loadAndPushMoviesToCache() {
        try {
            pushMoviesToCache(loadMoviesFromNet(), getBeginRowNumberForCurrentPage())
        } catch (t: Throwable) {
            Timber.d(t.localizedMessage)
        }
    }

    private fun getBeginRowNumberForCurrentPage() = currentPage * pageSize

    private suspend fun loadMoviesFromNet() =
            remoteAPIRepository.loadMovies(page = currentPage + 1).map { movieResponse ->
                mapper.createMovieInstance(movieResponse, true)
            }

    private suspend fun loadGenresFromNet() =
            remoteAPIRepository.loadGenres().map {
                mapper.createGenreInstance(it)
            }

    private suspend fun getRuntimeValueForMovie(movieID: Int) =
            if (runtimesCache.containsKey(movieID)) {
                runtimesCache[movieID]!!
            } else {
                val value = remoteAPIRepository.loadMovie(movieID)?.runtime?.toInt() ?: 0
                runtimesCache.put(movieID, value)
                value
            }

    private suspend fun getMovieActors(movieID: Int) = remoteAPIRepository.loadMovieActors(movieID)
            .map { castResponse ->
                Actor(
                        id = castResponse.id.toInt(),
                        name = castResponse.name,
                        picture = castResponse.profilePath
                )
            }

    private suspend fun pushGenresCache(genres: List<Genre>) {
        movieDatabase.genresDAO.insertAll(genres.map {
            mapper.createGenreEntityInstance(it)
        })
    }

    private suspend fun loadGenres() {
        genresCache.addAll(movieDatabase.genresDAO.getAll().map {
            mapper.createGenreInstance(it)
        })

        if (genresCache.isEmpty()) {
            try {
                genresCache.addAll(loadGenresFromNet())
                pushGenresCache(genresCache)
            } catch (t: Throwable) {
                Timber.d(t.localizedMessage)
            }
        } else {
            GlobalScope.launch {
                try {
                    pushGenresCache(loadGenresFromNet())
                } catch (t: Throwable) {
                    Timber.d(t.localizedMessage)
                }

                genresCache.addAll(movieDatabase.genresDAO.getAll().map {
                    mapper.createGenreInstance(it)
                })
            }
        }
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

        suspend fun createMovieInstance(
                from: MovieDetailResponse,
                loadRuntimeValue: Boolean
        ): Movie {
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

        suspend fun createMovieInstance(
                from: MovieEntity
        ): Movie {
            val genresEntity = movieDatabase.genresDAO.getMovieGenres(from.movieID)
            val genres = genresEntity.map {
                mapper.createGenreInstance(it)
            }

            return Movie(
                    from.movieID,
                    from.title,
                    from.overview,
                    poster = from.poster ?: "",
                    backdrop = from.backdrop ?: "",
                    from.ratings,
                    from.adult,
                    from.runtime,
                    genres,
                    emptyList(),
                    from.voteCount,
                    from.isLiked
            )
        }

        fun createMovieEntityInstance(from: Movie, position: Int) =
                MovieEntity(
                        movieID = from.id,
                        position = position,
                        title = from.title,
                        overview = from.overview,
                        poster = from.poster,
                        backdrop = from.backdrop,
                        ratings = from.ratings,
                        adult = from.adult,
                        runtime = from.runtime,
                        voteCount = from.voteCount,
                        isLiked = from.isLiked
                )

        fun createGenreInstance(from: GenreResponse): Genre =
                Genre(from.id.toInt(), from.name)

        fun createGenreInstance(from: GenresEntity): Genre =
                Genre(from.id?.toInt() ?: -1, from.name)

        fun createGenreEntityInstance(from: Genre): GenresEntity =
                GenresEntity(id = from.id.toLong(), name = from.name)

        fun createActorInstance(from: ActorsEntity): Actor =
                Actor(
                        from.id?.toInt() ?: -1,
                        from.name,
                        from.picturePath
                )
    }

    companion object {
        fun getInstance(
                remoteAPIRepository: RemoteAPIRepository,
                movieDatabase: MoviesDatabase
        ) = MoviesInteractor(remoteAPIRepository, movieDatabase)
    }
}