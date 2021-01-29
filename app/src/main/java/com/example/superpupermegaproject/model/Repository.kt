package com.example.superpupermegaproject.model

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

class Repository private constructor(
    private val remoteAPIRepository: RemoteAPIRepository,
    private val movieDatabase: MoviesDatabase
) {
    private val pageSize = RemoteAPIRepository.PAGE_SIZE
    private val genresCache: MutableList<Genre> = mutableListOf() //кэширование всех доступных жанров
    private val runtimesCache = mutableMapOf<Int, Int>() //кэширование продолжительности фильмов
    private val mapper = Mapper() //Преобразование Movie в типы response/entity и обратно

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
    suspend fun getMovies(page: Long): List<Movie> {
        val result = mutableListOf<Movie>()

        result.addAll(getMoviesFromCache(page)) //запрос данных для currentPage из кэша

        if (result.isEmpty()) {
            try {
                val movies = loadMoviesFromNet(page) //запрос данных для currentPage из API
                result.addAll(movies)
            } catch (t: Throwable) {
                Timber.d(t.localizedMessage)
            }
            pushMoviesToCache(result, page) //помещение в кэш
        } else {
            GlobalScope.launch {
                loadAndPushMoviesToCache(page) //обновление в кэше в параллельном потоке
            }
        }

        return result
    }

    //Получение данных фильма из API
    suspend fun getMovie(id: Int): Movie? {
        val movieResponse = remoteAPIRepository.loadMovie(id)
        if (movieResponse == null)
            return null

        return mapper.createMovieInstance(movieResponse)
    }

    suspend fun loadAndPushMoviesToCache(page: Long) {
        try {
            pushMoviesToCache(loadMoviesFromNet(page), getBeginRowNumberForCurrentPage(page))
        } catch (t: Throwable) {
            Timber.d(t.localizedMessage)
        }
    }

    suspend fun clearCache() {
        movieDatabase.moviesDAO.deleteAll()
    }

    fun getPageCountFromAPI() = remoteAPIRepository.maxPage

    private suspend fun getMoviesFromCache(page: Long): List<Movie> =
        movieDatabase.moviesDAO.getAllByPage(getBeginRowNumberForCurrentPage(page - 1), pageSize).map {
            mapper.createMovieInstance(it)
        }

    private suspend fun pushMoviesToCache(movies: List<Movie>, page: Long) {
        var curPosition = getBeginRowNumberForCurrentPage(page)
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

    private fun getBeginRowNumberForCurrentPage(page: Long) = page * pageSize

    private suspend fun loadMoviesFromNet(page: Long) =
        remoteAPIRepository.loadMovies(page = page).map { movieResponse ->
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
            from: MovieDetailResponse
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

        fun createMovieEntityInstance(from: Movie, position: Long) =
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
        ) = Repository(remoteAPIRepository, movieDatabase)
    }
}