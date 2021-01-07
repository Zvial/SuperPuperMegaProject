package com.example.superpupermegaproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.Repository

class MovieDetailsViewModel(val repository: Repository) : ViewModel() {
    private val _movieObservable = MutableLiveData<Movie>()
    val movieObservable: LiveData<Movie> = _movieObservable

    suspend fun getMovie(id: Long) {
        _movieObservable.value = repository.getMovie(id.toInt())
    }
}