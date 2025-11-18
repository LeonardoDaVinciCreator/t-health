package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.ActiveStorage
import com.tbank.t_health.data.local.StepCounterService
import com.tbank.t_health.domain.model.DailyStats

class GetTodayStatsUseCase(
    private val service: StepCounterService,
    private val storage: ActiveStorage
) {
    suspend operator fun invoke(): DailyStats {
        val steps = service.getStepsForToday()
        val activeMinutes = service.getActiveMinutesForToday() + storage.getActiveSeconds() / 60
        val calories = service.getCaloriesFromStepsAndActiveCalories() + storage.getCalories()

        return DailyStats(steps, activeMinutes, calories)
    }
}
