package com.tbank.t_health.ui.notifications

import androidx.lifecycle.ViewModel
import com.tbank.t_health.domain.usecase.GetNotificationsUseCase
import com.tbank.t_health.domain.usecase.MarkNotificationsAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotifications: GetNotificationsUseCase,
    private val markAsRead: MarkNotificationsAsReadUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow<List<NotificationsListItem>>(emptyList())
    val state: StateFlow<List<NotificationsListItem>> = _state

    init {
        load()
    }

    private fun load() {
        val all = getNotifications()

        val unread = all.filter { !it.isRead }
        val read = all.filter { it.isRead }

        val result = buildList {
            if (unread.isNotEmpty()) {
                add(NotificationsListItem.Header("Новые"))
                unread.forEach { add(NotificationsListItem.Item(it)) }
            }
            if (read.isNotEmpty()) {
                add(NotificationsListItem.Header("Просмотренные"))
                read.forEach { add(NotificationsListItem.Item(it)) }
            }
        }

        _state.value = result
        markAsRead()
    }
}
