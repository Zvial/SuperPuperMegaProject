package com.example.superpupermegaproject

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.example.superpupermegaproject.model.network.api_responses.RemoteAPIRepository
import com.example.superpupermegaproject.model.MoviesInteractor
import com.example.superpupermegaproject.model.FilesRepository
import com.example.superpupermegaproject.model.Repository
import com.example.superpupermegaproject.model.WorkInteractor
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
        private var repositoryInstance: Repository? = null
        private var remoteAPIRepositoryInstance: RemoteAPIRepository? = null
        private var moviesDatabase: MoviesDatabase? = null
        private var workInteractorInstance: WorkInteractor? = null
        private var filesRepositoryInstance: FilesRepository? = null

        fun getMoviesInteractorInstance(): MoviesInteractor {
            var tmpInstance = moviesInteractorInstance

            if (tmpInstance == null) {
                tmpInstance = MoviesInteractor.getInstance(getRepositoryInstance())
            }

            moviesInteractorInstance = tmpInstance

            return tmpInstance
        }

        private fun getAPIRepositoryInstance(): RemoteAPIRepository {
            var tmpInstance = remoteAPIRepositoryInstance

            if (tmpInstance == null) {
                tmpInstance = RemoteAPIRepository(apiKey)
            }

            remoteAPIRepositoryInstance = tmpInstance

            return tmpInstance
        }

        fun getRepositoryInstance(): Repository {
            var tmpInstance = repositoryInstance

            if (tmpInstance == null) {
                tmpInstance = Repository.getInstance(getAPIRepositoryInstance(), getMoviesDatabaseInstance())
            }

            repositoryInstance = tmpInstance

            return tmpInstance
        }

        fun getWorkInteractorInstance(): WorkInteractor {
            var tmpInstance = workInteractorInstance

            if (tmpInstance == null) {
                tmpInstance = WorkInteractor(WorkManager.getInstance(appContext))
            }

            workInteractorInstance = tmpInstance

            return tmpInstance
        }

        fun getPrefsRepositoryInstance(): FilesRepository {
            var tmpInstance = filesRepositoryInstance

            if (tmpInstance == null) {
                tmpInstance = FilesRepository(appContext)
            }

            filesRepositoryInstance = tmpInstance

            return tmpInstance
        }

        private fun getMoviesDatabaseInstance() = MoviesDatabase.getInstance(appContext)
    }
}