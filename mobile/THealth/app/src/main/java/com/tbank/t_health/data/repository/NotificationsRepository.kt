package com.tbank.t_health.data.repository

import com.tbank.t_health.data.local.NotificationStorage
import com.tbank.t_health.data.model.LocalNotification
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationsRepository  @Inject constructor(
    private val storage: NotificationStorage
) {
    private val _hasUnread = MutableStateFlow(storage.hasUnread())
    val hasUnread: StateFlow<Boolean> = _hasUnread

    fun refresh() {
        _hasUnread.value = storage.hasUnread()
    }

    fun getNotificationsLast28Days(): List<LocalNotification> {
        val now = System.currentTimeMillis()
        val cutoff = now - 28L * 24 * 60 * 60 * 1000

        return storage.getAll()
            .filter { it.timestamp >= cutoff }
            .sortedByDescending { it.timestamp }
    }

    fun markAllAsRead() = storage.markAllAsRead()

    fun hasUnread(): Boolean = storage.hasUnread()

    fun hasUnreadFlow(): Flow<Boolean> = flow {
        while (true) {
            emit(hasUnread())
            delay(1000) // 1 секунда для обновления
        }
    }

}
