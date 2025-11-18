package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.StepCounterService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveStepsUseCase(
    private val service: StepCounterService
) {
    operator fun invoke(): Flow<Int> = flow {
        while (true) {
            emit(service.getStepsForToday())
            delay(5000) //каждые 5 секунд
        }
    }
}
