package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.UserPrefs
import com.tbank.t_health.data.repository.ActivityRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTodayTrainingCaloriesUseCase @Inject constructor(
    private val repo: ActivityRepository,
    private val prefs: UserPrefs
) {
    suspend operator fun invoke(): Double {
        val user = prefs.getUser() ?: return 0.0
        val userId = user.id ?: return 0.0

        return repo.getTrainingCaloriesForDate(
            userId = userId,
            date = LocalDate.now()
        )
    }
}
