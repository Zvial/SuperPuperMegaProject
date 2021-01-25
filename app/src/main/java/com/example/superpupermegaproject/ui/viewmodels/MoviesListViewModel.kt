
package com.example.superpupermegaproject.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.model.pagination.MovieDataSourceFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MoviesListViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {
    private val pageSize = 20
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val _stateObservable = MutableLiveData<MovieResultState>()
    private var isFirstQueryValue = true

    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()
    private val datasourceFactory = MovieDataSourceFactory(moviesInteractor, viewModelScope, _stateObservable)
    var moviesObservable = LivePagedListBuilder<Int, Movie>(datasourceFactory, pagedListConfig)
        .setBoundaryCallback(BoundaryCallback(moviesInteractor, viewModelScope, datasourceFactory))
        .build()
        private set
    val stateObservable: LiveData<MovieResultState> = _stateObservable
    val queryChannel = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryChannel
                .debounce(400)
                .collect {
                    if(!isFirstQueryValue) {
                        datasourceFactory.query = it
                        datasourceFactory.dataSource.value?.invalidate()
                    }
                    isFirstQueryValue = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    /*
    fun searchTextChanged(query: String) {
        viewModelScope.launch {
            delay(200)
            datasourceFactory.query = query
            datasourceFactory.dataSource.value?.invalidate()
        }
    }
     */

    suspend fun updateMovies() {
        datasourceFactory.dataSource.value?.invalidate()
    }
}