package com.example.superpupermegaproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.fundamentals.homework.features.data.Movie
import com.example.superpupermegaproject.data.MoviesInteractor

class MovieDetailsViewModel(app: Application) : AndroidViewModel(app) {
    private val moviesInteractor = MoviesInteractor(getApplication())
    private val _movieObservable = MutableLiveData<Movie>()
    val movieObservable: LiveData<Movie> = _movieObservable

    suspend fun getMovie(id: Long) {
        _movieObservable.value = moviesInteractor
            .getMoviesList()
            .firstOrNull {
                it.id == id
            }
    }
}