package com.example.superpupermegaproject.model.work_manager

import android.content.Context
import androidx.work.*
import com.example.superpupermegaproject.App
import timber.log.Timber

class UpdateCacheWork(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    private val repository by lazy { App.getRepositoryInstance() }
    private val prefsRepository by lazy { App.getPrefsRepositoryInstance() }

    override suspend fun doWork(): Result {
        try {
            repository.clearCache()
            Timber.d("Кэш очищен")
            prefsRepository.writeToFileLog("Кэш очищен")
        } catch (t: Throwable) {
            Timber.d("Ошибка очистки кэша" +
                    " error: ${t.localizedMessage}")
            prefsRepository.writeToFileLog("Ошибка очистки кэша" +
                    " error: ${t.localizedMessage}")
            return Result.retry()
        }

        var lastLoadedPage = 0L
        try {
            for (page in 0..repository.getPageCountFromAPI()) {
                repository.loadAndPushMoviesToCache(page)
                lastLoadedPage = page
            }
            Timber.d("Кэш обновлен")
            prefsRepository.writeToFileLog("Кэш обновлен")
        } catch (t: Throwable) {
            Timber.d("Ошибка заполнения кэша" +
                    " page = $lastLoadedPage " +
                    " error: ${t.localizedMessage}")
            prefsRepository.writeToFileLog("Ошибка заполнения кэша" +
                    " page = $lastLoadedPage " +
                    " error: ${t.localizedMessage}")
            return Result.retry()
        }

        return Result.success()
    }
}