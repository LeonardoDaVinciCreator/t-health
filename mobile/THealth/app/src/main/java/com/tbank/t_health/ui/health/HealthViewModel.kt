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

    private val _stepsGoal = MutableStateFlow(8000f)
    val stepsGoal: StateFlow<Float> = _stepsGoal

    private val _activeMinutesGoal = MutableStateFlow(120f) // 2 часа
    val activeMinutesGoal: StateFlow<Float> = _activeMinutesGoal

    private val _caloriesGoal = MutableStateFlow(500f)
    val caloriesGoal: StateFlow<Float> = _caloriesGoal


    val activeMinutesProgress: StateFlow<Float> = combine(
        todayStats,
        _activeMinutesGoal
    ) { stats, goal ->
        if (goal <= 0f) 0f
        else (stats.activeMinutes.toFloat() / goal).coerceIn(0f, 1f)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)


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

                // После синхронизации обновляем данные, нужно исправить тк это должно быть в ui
                delay(1000) // Даем время на сохранение данных
                loadTodayData()
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Ошибка синхронизации", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun formatActiveMinutes(minutes: Long): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%d:%02d", hours, mins)
    }
}
