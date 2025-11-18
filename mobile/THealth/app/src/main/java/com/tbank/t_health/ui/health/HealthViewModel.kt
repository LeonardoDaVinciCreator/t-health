package com.tbank.t_health.ui.health

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.domain.model.DailyStats
import com.tbank.t_health.domain.usecase.*
import com.tbank.t_health.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val getTodayStats: GetTodayStatsUseCase,
    private val getYesterdayStats: GetYesterdayStatsUseCase,
    private val observeSteps: ObserveStepsUseCase,
    private val syncActivities: SyncActivitiesUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _todayStats = MutableStateFlow(DailyStats.empty())
    val todayStats: StateFlow<DailyStats> = _todayStats.asStateFlow()

    private val _yesterdaySteps = MutableStateFlow(0)
    val yesterdaySteps: StateFlow<Int> = _yesterdaySteps.asStateFlow()

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUser()
        observeTodaySteps()
        loadYesterday()
        loadTodayData()

    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                _user.value = getUserUseCase()
                Log.d("HealthViewModel", "Пользователь загружен: ${_user.value}")
                _user.value?.id?.let { userId ->
                    Log.d("HealthViewModel", "Пользователь найден, ID: $userId")
                } ?: run {
                    Log.e("HealthViewModel", "Пользователь не найден")
                }
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Ошибка загрузки пользователя", e)
            }
        }
    }

    private fun observeTodaySteps() {
        viewModelScope.launch {
            observeSteps().collect { steps ->
                _todayStats.update { it.copy(steps = steps) }
            }
        }
    }

    private fun loadToday() {
        viewModelScope.launch {
            _todayStats.value = getTodayStats()
        }
    }

    private fun loadYesterday() {
        viewModelScope.launch {
            _yesterdaySteps.value = getYesterdayStats()
        }
    }

    //(шаги, активные минуты, калории) и синхронизации с сервером
    fun loadTodayData() {
        viewModelScope.launch {
            _todayStats.value = getTodayStats()
        }
    }

    //Синхронизация с сервером
    fun sync(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("HealthViewModel", "Начало синхронизации для userId: $userId")
                syncActivities(userId)
                Log.d("HealthViewModel", "Синхронизация завершена")

                // После синхронизации обновляем данные
                delay(1000) // Даем время на сохранение данных
                loadTodayData()
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Ошибка синхронизации", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
