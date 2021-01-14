package com.example.superpupermegaproject.ui.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MoviesPositionalDataSource(val moviesInteractor: MoviesInteractor,
                                 val scope: CoroutineScope,
                                 private val stateObservable: MutableLiveData<MovieResultState>
) : PositionalDataSource<Movie>() {
    private val START_PAGE_NUMBER = 0
    private var lastLoadedPage = START_PAGE_NUMBER

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        scope.launch {
            callback.onResult(execute(lastLoadedPage), lastLoadedPage)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        val page = params.startPosition/20
        scope.launch {
            callback.onResult(execute(page))
        }
    }

    private suspend fun execute(page: Int): List<Movie> {
        val result = mutableListOf<Movie>()
        val isFirstPage = page == START_PAGE_NUMBER
        if (isFirstPage) {
            stateObservable.postValue(MovieResultState.Loading)
        }
        else {
            stateObservable.postValue(MovieResultState.LoadingNextPage)
        }
        try {
            result.addAll(moviesInteractor.getMovies(page))
            stateObservable.postValue(MovieResultState.Sucess)
            lastLoadedPage = page
            Log.d("LOG", "Загружена страница $lastLoadedPage" )
        } catch (t: Throwable) {
            if(isFirstPage)
                stateObservable.postValue(MovieResultState.Error(t))
            else
                stateObservable.postValue(MovieResultState.ErrorNextPage(t))
        }
        return result
    }
}