package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.repository.NotificationsRepository
import javax.inject.Inject

class MarkNotificationsAsReadUseCase  @Inject constructor(
    private val repository: NotificationsRepository
) {
    operator fun invoke() {
        repository.markAllAsRead()
    }
}
