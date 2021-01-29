package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.model.WorkInteractor

class MoviesListViewModelFactory(
        val moviesInteractor: MoviesInteractor) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            when (modelClass) {
                MoviesListViewModel::class.java -> MoviesListViewModel(moviesInteractor) as T
                else -> throw(Exception("Некорректный класс ViewModel"))
            }
}