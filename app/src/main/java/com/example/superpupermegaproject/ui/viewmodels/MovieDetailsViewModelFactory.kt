package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.superpupermegaproject.model.MoviesInteractor

class MovieDetailsViewModelFactory(val moviesInteractor: MoviesInteractor): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when(modelClass) {
            MovieDetailsViewModel::class.java -> MovieDetailsViewModel(moviesInteractor) as T
            else -> throw(Exception("Некорректный класс ViewModel"))
        }
}