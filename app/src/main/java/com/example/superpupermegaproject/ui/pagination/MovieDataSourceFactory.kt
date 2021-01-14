package com.example.superpupermegaproject.ui.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope

class MovieDataSourceFactory(private val moviesInteractor: MoviesInteractor,
                             private val scope: CoroutineScope,
                             private val stateObservable: MutableLiveData<MovieResultState>
                             ) : DataSource.Factory<Int, Movie>() {
    val dataSource = MutableLiveData<MoviesPositionalDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = MoviesPositionalDataSource(moviesInteractor, scope, stateObservable)
        dataSource.postValue(source)
        return source
    }
}