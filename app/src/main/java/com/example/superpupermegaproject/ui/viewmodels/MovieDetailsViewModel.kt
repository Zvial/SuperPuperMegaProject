package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MoviesInteractor

class MovieDetailsViewModel(val moviesInteractor: MoviesInteractor) : ViewModel() {
    private val _movieObservable = MutableLiveData<Movie>()
    val movieObservable: LiveData<Movie> = _movieObservable

    suspend fun getMovie(id: Long) {
        _movieObservable.value = moviesInteractor.getMovie(id.toInt())
    }
}