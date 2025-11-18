package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.StepCounterService
import java.time.LocalDate

class GetYesterdayStatsUseCase(
    private val service: StepCounterService
) {
    suspend operator fun invoke(): Int {
        return service.getStepsForDate(LocalDate.now().minusDays(1))
    }
}
