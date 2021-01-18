package com.example.superpupermegaproject.ui.pagination

import androidx.lifecycle.MutableLiveData
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope

class MoviesListDataSource(
    moviesInteractor: MoviesInteractor,
    scope: CoroutineScope,
    stateObservable: MutableLiveData<MovieResultState>
) : MoviesPositionalDataSource(moviesInteractor, scope, stateObservable) {
    override suspend fun loadMovies(isInitial: Boolean): List<Movie> =
        moviesInteractor.getMovies(isInitial)
}