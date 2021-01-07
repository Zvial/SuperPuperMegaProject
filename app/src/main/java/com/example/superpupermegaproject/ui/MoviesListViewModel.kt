package com.example.superpupermegaproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.Repository

class MoviesListViewModel(val repository: Repository) : ViewModel() {
    private val _moviesObservable = MutableLiveData<List<Movie>>()
    val moviesObservable: LiveData<List<Movie>> = _moviesObservable

    suspend fun loadMovies() {
        _moviesObservable.value = repository.getMovies()
    }
}