package com.example.superpupermegaproject.ui.viewmodels

import androidx.paging.PagedList
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.model.pagination.MovieDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class BoundaryCallback(
    val moviesInteractor: MoviesInteractor,
    val scope: CoroutineScope,
    val dataSourceFactory: MovieDataSourceFactory): PagedList.BoundaryCallback<Movie>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()

        Timber.d("BoundaryCallback.onZeroItemsLoaded()")

        scope.launch {
            //moviesInteractor.loadAndPushMoviesToCache()
            //dataSourceFactory.dataSource.value?.invalidate()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Movie) {
        super.onItemAtFrontLoaded(itemAtFront)

        Timber.d("BoundaryCallback.onItemAtFrontLoaded()")
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        super.onItemAtEndLoaded(itemAtEnd)

        Timber.d("BoundaryCallback.onItemAtEndLoaded()")

        scope.launch {
            //moviesInteractor.loadAndPushMoviesToCache()
            //dataSourceFactory.dataSource.value?.invalidate()
        }
    }
}