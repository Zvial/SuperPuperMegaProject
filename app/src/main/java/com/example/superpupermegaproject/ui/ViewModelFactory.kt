package com.example.superpupermegaproject.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(val appContext: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when(modelClass) {
            MoviesListViewModel::class.java -> MoviesListViewModel(appContext) as T
            MovieDetailsViewModel::class.java -> MovieDetailsViewModel(appContext) as T
            else -> throw(Exception("Некорректный класс ViewModel"))
        }

}