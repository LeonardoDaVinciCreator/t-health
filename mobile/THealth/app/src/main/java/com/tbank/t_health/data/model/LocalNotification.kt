package com.tbank.t_health.data.model

data class LocalNotification(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false
)

enum class NotificationType {
    STEPS, ACTIVE_MINUTES, CALORIES
}