package com.tbank.t_health.ui.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tbank.t_health.data.local.UserPrefs
import com.tbank.t_health.data.model.LocalNotification

@Composable
fun NotificationsScreen() {
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context) }

    // Загружаем список уведомлений
    var notifications by remember { mutableStateOf(prefs.getNotifications()) }

    // Помечаем все как прочитанные при открытии экрана
    LaunchedEffect(Unit) {
        prefs.markAllNotificationsAsRead()
        notifications = prefs.getNotifications() // перезагружаем, чтобы обновить статус
    }

    // Простой список
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(notifications) { notif ->
            NotificationItemSimple(notification = notif)
        }
    }
}

@Composable
fun NotificationItemSimple(notification: LocalNotification) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = notification.title)
        Text(text = notification.description)
        Text(text = notification.timestamp.toString())
    }
}
