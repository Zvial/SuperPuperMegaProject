package com.example.superpupermegaproject

import android.app.Application
import android.content.Context
import com.example.superpupermegaproject.model.network.api_responses.RemoteAPIRepository
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.model.database.entities.MoviesDatabase
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initApiKey()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        val tree = Timber.DebugTree()
        Timber.plant(tree)

        appContext = base!!
    }

    private fun initApiKey() {
        try {
            val stream = assets.open("api_key")
            apiKey = stream.reader().readText().trim()
            stream.close()
        } catch (t: Throwable) {
            throw Exception("Не доступен файл api_key в каталоге assets. ${t.localizedMessage}")
        }
    }

    companion object {
        lateinit var appContext: Context

        private var apiKey: String = ""
        private var moviesInteractorInstance: MoviesInteractor? = null
        private var remoteAPIRepositoryInstance: RemoteAPIRepository? = null
        private var moviesDatabase: MoviesDatabase? = null

        fun getMoviesInteractorInstance(): MoviesInteractor =
            if (moviesInteractorInstance == null) {
                MoviesInteractor.getInstance(getRepositoryInstance(), getMoviesDatabaseInstance())
            } else {
                moviesInteractorInstance!!
            }

        private fun getRepositoryInstance(): RemoteAPIRepository =
            if (remoteAPIRepositoryInstance == null) {
                RemoteAPIRepository(apiKey)
            } else {
                remoteAPIRepositoryInstance!!
            }

        private fun getMoviesDatabaseInstance() = MoviesDatabase.getInstance(appContext)
    }
}