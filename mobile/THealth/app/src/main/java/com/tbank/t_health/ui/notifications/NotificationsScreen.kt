package com.tbank.t_health.ui.notifications

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val items by viewModel.state.collectAsState()

    LazyColumn {
        items(items) { item ->
            when (item) {
                is NotificationsListItem.Header -> {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                }
                is NotificationsListItem.Item -> {
                    NotificationItem(item.notification)
                }
            }
        }
    }
}
