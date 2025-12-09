package com.tbank.t_health.ui.notifications

import com.tbank.t_health.domain.model.NotificationUiModel

sealed interface NotificationsListItem {
    data class Header(val title: String) : NotificationsListItem
    data class Item(val notification: NotificationUiModel) : NotificationsListItem
}
