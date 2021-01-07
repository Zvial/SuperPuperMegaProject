package com.example.superpupermegaproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.superpupermegaproject.model.Repository

class ViewModelFactory(val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when(modelClass) {
            MoviesListViewModel::class.java -> MoviesListViewModel(repository) as T
            MovieDetailsViewModel::class.java -> MovieDetailsViewModel(repository) as T
            else -> throw(Exception("Некорректный класс ViewModel"))
        }
}