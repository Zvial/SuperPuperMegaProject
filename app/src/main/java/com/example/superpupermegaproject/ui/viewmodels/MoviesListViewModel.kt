
package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.ui.pagination.MovieDataSourceFactory
import com.example.superpupermegaproject.ui.pagination.MoviesPositionalDataSource
import kotlinx.coroutines.*

class MoviesListViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {
    private val pageSize = 20
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val _stateObservable = MutableLiveData<MovieResultState>()

    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()
    private val datasourceFactory = MovieDataSourceFactory(moviesInteractor, viewModelScope, _stateObservable)
    val moviesObservable = LivePagedListBuilder<Int, Movie>(datasourceFactory, pagedListConfig).build()
    val stateObservable: LiveData<MovieResultState> = _stateObservable

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    suspend fun updateMovies() {
        datasourceFactory.dataSource.value?.invalidate()
    }

}