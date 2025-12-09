package com.tbank.t_health.data.model

import java.time.LocalDate

data class LocalNotification(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val dayKey: String, //для того чтобы одно и то же сообщее не выводилось "2025-03-09"
    var isRead: Boolean = false
)

enum class NotificationType {
    STEPS, ACTIVE_MINUTES, CALORIES
}