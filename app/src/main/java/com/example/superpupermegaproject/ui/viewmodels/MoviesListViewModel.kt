
package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MoviesListViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {
    private val pageSize = 20
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()
    private val datasourceFactory = object : DataSource.Factory<Int, Movie>() {
        override fun create(): DataSource<Int, Movie> = MoviesPositionalDataSource(moviesInteractor, viewModelScope)
    }
    val moviesObservable = LivePagedListBuilder<Int, Movie>(datasourceFactory, pagedListConfig).build()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    class MoviesPositionalDataSource(val moviesInteractor: MoviesInteractor, val scope: CoroutineScope): PositionalDataSource<Movie>() {
        private val START_PAGE_NUMBER = 0

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
            scope.launch {
                callback.onResult(moviesInteractor.getMovies(START_PAGE_NUMBER), START_PAGE_NUMBER)
            }
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
            scope.launch {
                callback.onResult(moviesInteractor.getMovies(params.startPosition))
            }
        }
    }
}