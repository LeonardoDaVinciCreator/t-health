package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.UserPrefs
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.domain.toDailyStats
import com.tbank.t_health.domain.model.DailyStats
import java.time.LocalDate

class GetTodayStatsFromRepoUseCase(
    private val repo: ActivityRepository,
    private val prefs: UserPrefs
) {
    suspend operator fun invoke(): DailyStats {
        val user = prefs.getUser() ?: return DailyStats.empty()
        val today = LocalDate.now()

        val userId = user.id ?: return DailyStats.empty()
        return repo.getUserActivityForDate(userId, today)
            ?.toDailyStats()
            ?: DailyStats.empty()
    }
}
