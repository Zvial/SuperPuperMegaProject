package com.example.superpupermegaproject.model

import androidx.work.*
import com.example.superpupermegaproject.model.work_manager.UpdateCacheWork
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkInteractor(
    private val workManager: WorkManager
) {
    private val UPDATE_CACHE_WORK_NAME = "com.example.superpupermegaproject.update_cache_work"

    fun createPeriodicWorkRequest(): WorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val request = PeriodicWorkRequestBuilder<UpdateCacheWork>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        Timber.d("Создан запрос WorkManager")

        return request
    }

    fun pushWorkRequestToEnqueue(workRequest: WorkRequest) {
        when (workRequest) {
            is PeriodicWorkRequest -> workManager.enqueueUniquePeriodicWork(
                UPDATE_CACHE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
            is OneTimeWorkRequest -> workManager.enqueueUniqueWork(
                UPDATE_CACHE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                workRequest
            )
        }
        Timber.d("Запрос поставлен в очередь")
    }

    fun cancelWorkRequest() {
        workManager.cancelUniqueWork(UPDATE_CACHE_WORK_NAME)
        Timber.d("Отменен запрос WorkManager")
    }
}