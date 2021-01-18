package com.example.superpupermegaproject.ui.pagination

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.model.MoviesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class MoviesPositionalDataSource(protected val moviesInteractor: MoviesInteractor,
                                            protected val scope: CoroutineScope,
                                            protected val stateObservable: MutableLiveData<MovieResultState>
) : PositionalDataSource<Movie>() {
    private val START_PAGE_NUMBER = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        scope.launch {
            callback.onResult(execute(true), START_PAGE_NUMBER)
            Log.d("LOG", "loadInitial ${this.javaClass.canonicalName}")
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        scope.launch {
            callback.onResult(execute(false))
            Log.d("LOG", "loadRange ${this.javaClass.canonicalName}")
        }
    }

    protected abstract suspend fun loadMovies(isInitial: Boolean): List<Movie>

    private suspend fun execute(isFirstPage: Boolean): List<Movie> {
        val result = mutableListOf<Movie>()
        if (isFirstPage) {
            stateObservable.postValue(MovieResultState.Loading)
        }
        else {
            stateObservable.postValue(MovieResultState.LoadingNextPage)
        }
        Log.d("LOG", "set state ${stateObservable.value}")
        try {
            result.addAll(loadMovies(isFirstPage))
            stateObservable.postValue(MovieResultState.Sucess)
        } catch (t: Throwable) {
            if(isFirstPage)
                stateObservable.postValue(MovieResultState.Error(t))
            else
                stateObservable.postValue(MovieResultState.ErrorNextPage(t))
        }
        Log.d("LOG", "set state ${stateObservable.value}")
        return result
    }
}