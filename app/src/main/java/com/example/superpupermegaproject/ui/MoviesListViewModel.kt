package com.example.superpupermegaproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.fundamentals.homework.features.data.Movie
import com.example.superpupermegaproject.data.MoviesInteractor

class MoviesListViewModel(app: Application) : AndroidViewModel(app) {
    private val moviesInteractor = MoviesInteractor.getInstance(getApplication())
    private val _moviesObservable = MutableLiveData<List<Movie>>()
    val moviesObservable: LiveData<List<Movie>> = _moviesObservable

    suspend fun loadMovies() {
        _moviesObservable.value = moviesInteractor.getMoviesList()
    }
}