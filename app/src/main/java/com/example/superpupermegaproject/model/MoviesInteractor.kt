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
        private val repository: Repository
) {
    private var currentPage: Long = 1L //загружаемая страница

    //Поиск фильмов по подстроке. Только из внешнего API
    suspend fun searchMovies(
            query: String,
            isNewQuery: Boolean,
            loadRuntimeValue: Boolean = false
    ): List<Movie> = repository.searchMovies(query, isNewQuery, loadRuntimeValue)

    //Получение списка фильмов. Если доступа к внешнему API нет, получает из локального кэша
    suspend fun getMovies(): List<Movie> = repository.getMovies(currentPage)
        .also { movies ->
            if (movies.isNotEmpty()) currentPage++
        }

    //Получение данных фильма из API
    suspend fun getMovie(id: Int): Movie? = repository.getMovie(id)

    companion object {
        fun getInstance(
                repository: Repository
        ) = MoviesInteractor(repository)
    }
}