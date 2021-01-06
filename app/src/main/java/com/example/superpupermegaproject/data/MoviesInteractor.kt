package com.example.superpupermegaproject.data

import android.content.Context
import com.android.academy.fundamentals.homework.features.data.loadMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesInteractor(private val appContext: Context) {

    suspend fun getMoviesList() = withContext(Dispatchers.IO) {
        loadMovies(appContext)
    }

        companion object {
        private var instance: MoviesInteractor? = null

        fun getInstance(appContext: Context): MoviesInteractor {
            val tmpInstance = instance
            if(tmpInstance==null) {
                return MoviesInteractor(appContext)
            } else {
                return tmpInstance
            }
        }
    }
    }