package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.repository.NotificationsRepository
import com.tbank.t_health.domain.model.NotificationUiModel
import javax.inject.Inject

class GetNotificationsUseCase  @Inject constructor(
    private val repository: NotificationsRepository
) {
    operator fun invoke(): List<NotificationUiModel> {
        return repository.getNotificationsLast28Days()
            .map { NotificationUiModel.from(it) }
    }
}
