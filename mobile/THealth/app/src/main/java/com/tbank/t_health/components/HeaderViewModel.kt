package com.tbank.t_health.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.repository.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HeaderViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    val hasUnreadNotifications = notificationsRepository.hasUnreadFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
