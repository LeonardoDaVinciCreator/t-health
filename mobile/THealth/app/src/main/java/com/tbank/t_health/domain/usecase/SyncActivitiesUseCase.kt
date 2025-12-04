package com.tbank.t_health.domain.usecase

import android.util.Log
import com.tbank.t_health.data.local.ActiveStorage
import com.tbank.t_health.data.local.StepCounterService
import com.tbank.t_health.data.repository.ActivityRepository

class SyncActivitiesUseCase(
    private val repo: ActivityRepository,
    private val stepService: StepCounterService,
    private val activeStorage: ActiveStorage
) {
    suspend operator fun invoke(userId: Long) {
        Log.d("ActivityRepository", "syncToServer() CALLED")
        repo.collectAndSaveDailyData(
            stepService = stepService,
            activeStorage = activeStorage,
            userId = userId
        )

        repo.syncToServer(userId = userId, clearAfterSync = true)
    }
}
