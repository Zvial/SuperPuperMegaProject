package com.example.superpupermegaproject.model.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope

class MovieDataSourceFactory(
    private val moviesInteractor: MoviesInteractor,
    private val scope: CoroutineScope,
    private val stateObservable: MutableLiveData<MovieResultState>
) : DataSource.Factory<Int, Movie>() {
    var query: String = ""
    set(value) {
        field = value
        createDataSourceInstance()
    }
    val dataSource = MutableLiveData<MoviesPositionalDataSource>()

    override fun create(): DataSource<Int, Movie> = createDataSourceInstance()

    private fun createDataSourceInstance(): DataSource<Int, Movie> {
        val source = if (query.isEmpty()) {
            MoviesListDataSource(moviesInteractor, scope, stateObservable)
        } else {
            MoviesSearchDataSource(moviesInteractor, scope, stateObservable, query)
        }

        dataSource.postValue(source)
        return source
    }
}