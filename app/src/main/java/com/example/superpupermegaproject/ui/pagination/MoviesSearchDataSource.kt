package com.example.superpupermegaproject.ui.pagination

import androidx.lifecycle.MutableLiveData
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope

class MoviesSearchDataSource(
    moviesInteractor: MoviesInteractor,
    scope: CoroutineScope,
    stateObservable: MutableLiveData<MovieResultState>,
    val query: String
) : MoviesPositionalDataSource(moviesInteractor, scope, stateObservable) {
    override suspend fun loadMovies(isInitial: Boolean): List<Movie> =
        moviesInteractor.searchMovies(query = query, isInitial)
}