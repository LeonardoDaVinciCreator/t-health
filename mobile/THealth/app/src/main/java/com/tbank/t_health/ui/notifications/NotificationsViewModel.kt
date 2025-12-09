package com.tbank.t_health.ui.notifications

import androidx.lifecycle.ViewModel
import com.tbank.t_health.domain.model.NotificationUiModel
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
        MutableStateFlow<List<NotificationUiModel>>(emptyList())
    val state: StateFlow<List<NotificationUiModel>> = _state

    init {
        load()
    }

    private fun load() {



        _state.value = getNotifications()
        markAsRead()
    }
}
