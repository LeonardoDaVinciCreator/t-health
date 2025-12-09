package com.tbank.t_health.data.repository

import com.tbank.t_health.data.local.NotificationStorage
import com.tbank.t_health.data.model.LocalNotification
import javax.inject.Inject

class NotificationsRepository  @Inject constructor(
    private val storage: NotificationStorage
) {

    fun getNotificationsLast28Days(): List<LocalNotification> {
        val now = System.currentTimeMillis()
        val cutoff = now - 28L * 24 * 60 * 60 * 1000

        return storage.getAll()
            .filter { it.timestamp >= cutoff }
            .sortedByDescending { it.timestamp }
    }

    fun markAllAsRead() = storage.markAllAsRead()

    fun hasUnread(): Boolean = storage.hasUnread()
}
