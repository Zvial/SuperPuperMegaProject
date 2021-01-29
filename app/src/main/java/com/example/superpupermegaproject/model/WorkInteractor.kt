package com.example.superpupermegaproject.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.work.*
import com.example.superpupermegaproject.model.work_manager.UpdateCacheWork
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkInteractor(
    private val workManager: WorkManager
): LifecycleObserver {
    private val UPDATE_CACHE_WORK_NAME = "com.example.superpupermegaproject.update_cache_work"

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startWorkRequest() {
        workManager.getWorkInfosForUniqueWorkLiveData(UPDATE_CACHE_WORK_NAME)
                .observeForever { list ->
                    val notWorked = list.filter { workInfo ->
                        workInfo.state != WorkInfo.State.CANCELLED
                    }.isEmpty()

                    if (notWorked)
                        pushWorkRequestToEnqueue(createPeriodicWorkRequest())
                }
    }

    fun cancelWorkRequest() {
        workManager.cancelUniqueWork(UPDATE_CACHE_WORK_NAME)
        Timber.d("Отменен запрос WorkManager")
    }

    private fun createPeriodicWorkRequest(): WorkRequest {
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

    private fun pushWorkRequestToEnqueue(workRequest: WorkRequest) {
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
}