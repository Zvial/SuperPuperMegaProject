package com.example.superpupermegaproject

import android.app.Application
import com.example.superpupermegaproject.model.Repository
import com.example.superpupermegaproject.model.MoviesInteractor

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initApiKey()
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
        private var apiKey: String = ""
        private var moviesInteractorInstance: MoviesInteractor? = null
        private var repositoryInstance: Repository? = null

        fun getMoviesInteractorInstance(): MoviesInteractor =
            if (moviesInteractorInstance == null) {
                MoviesInteractor.getInstance(getRepositoryInstance())
            } else {
                moviesInteractorInstance!!
            }

        private fun getRepositoryInstance(): Repository =
            if (repositoryInstance == null) {
                Repository(apiKey)
            } else {
                repositoryInstance!!
            }
    }
}