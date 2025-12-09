package com.tbank.t_health.domain.model

import android.text.format.DateUtils
import com.tbank.t_health.data.model.LocalNotification
import com.tbank.t_health.data.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NotificationUiModel(
    val id: Long,
    val iconType: NotificationType,
    val title: String,
    val description: String,
    val timeText: String?,
    val isRead: Boolean
) {
    companion object {
        fun from(n: LocalNotification): NotificationUiModel {
            val isToday =
                DateUtils.isToday(n.timestamp)

            return NotificationUiModel(
                id = n.id,
                iconType = n.type,
                title = n.title,
                description = n.description,
                timeText = if (isToday)
                    SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date(n.timestamp))
                else null,
                isRead = n.isRead
            )
        }
    }
}
